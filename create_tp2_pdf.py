import os
import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml import OxmlElement, parse_xml
from docx.oxml.ns import nsdecls, qn
import win32com.client

def set_cell_background(cell, hex_color):
    shading_elm = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{hex_color}"/>')
    cell._tc.get_or_add_tcPr().append(shading_elm)

def set_cell_margins(cell, top=120, bottom=120, left=160, right=160):
    tcPr = cell._tc.get_or_add_tcPr()
    tcMar = OxmlElement('w:tcMar')
    for m, val in [('top', top), ('bottom', bottom), ('left', left), ('right', right)]:
        node = OxmlElement(f'w:{m}')
        node.set(qn('w:w'), str(val))
        node.set(qn('w:type'), 'dxa')
        tcMar.append(node)
    tcPr.append(tcMar)

def add_callout(doc, title, text, bg_color="F0F4F8", border_color="0F4C81"):
    tbl = doc.add_table(rows=1, cols=1)
    tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    cell = tbl.cell(0, 0)
    set_cell_background(cell, bg_color)
    set_cell_margins(cell, top=160, bottom=160, left=240, right=200)
    
    # Left border only
    tcPr = cell._tc.get_or_add_tcPr()
    borders = parse_xml(f'''
        <w:tcBorders {nsdecls("w")}>
            <w:top w:val="none"/>
            <w:left w:val="single" w:sz="24" w:space="0" w:color="{border_color}"/>
            <w:bottom w:val="none"/>
            <w:right w:val="none"/>
        </w:tcBorders>
    ''')
    tcPr.append(borders)
    
    p = cell.paragraphs[0]
    p.paragraph_format.space_before = Pt(0)
    p.paragraph_format.space_after = Pt(4)
    run_t = p.add_run(f"📌 {title}\n")
    run_t.bold = True
    run_t.font.name = "Calibri"
    run_t.font.size = Pt(11)
    run_t.font.color.rgb = RGBColor(15, 76, 129)
    
    run_b = p.add_run(text)
    run_b.font.name = "Calibri"
    run_b.font.size = Pt(10)
    run_b.font.color.rgb = RGBColor(45, 55, 72)
    doc.add_paragraph() # Spacing

def format_table(tbl, col_widths=None, header_bg="0F4C81", alt_bg="F8FAFC"):
    tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    for i, row in enumerate(tbl.rows):
        is_header = (i == 0)
        for j, cell in enumerate(row.cells):
            set_cell_margins(cell, top=120, bottom=120, left=140, right=140)
            cell.vertical_alignment = WD_ALIGN_VERTICAL.CENTER
            if col_widths and j < len(col_widths):
                cell.width = Inches(col_widths[j])
            
            if is_header:
                set_cell_background(cell, header_bg)
                for p in cell.paragraphs:
                    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
                    for r in p.runs:
                        r.bold = True
                        r.font.name = "Calibri"
                        r.font.size = Pt(10)
                        r.font.color.rgb = RGBColor(255, 255, 255)
            else:
                if i % 2 == 0:
                    set_cell_background(cell, alt_bg)
                else:
                    set_cell_background(cell, "FFFFFF")
                for p in cell.paragraphs:
                    for r in p.runs:
                        r.font.name = "Calibri"
                        r.font.size = Pt(9.5)
                        r.font.color.rgb = RGBColor(45, 55, 72)
            
            # Subtle cell border
            tcPr = cell._tc.get_or_add_tcPr()
            borders = parse_xml(f'''
                <w:tcBorders {nsdecls("w")}>
                    <w:top w:val="single" w:sz="4" w:space="0" w:color="E2E8F0"/>
                    <w:left w:val="single" w:sz="4" w:space="0" w:color="E2E8F0"/>
                    <w:bottom w:val="single" w:sz="4" w:space="0" w:color="E2E8F0"/>
                    <w:right w:val="single" w:sz="4" w:space="0" w:color="E2E8F0"/>
                </w:tcBorders>
            ''')
            tcPr.append(borders)

def build_tp2_document():
    doc = docx.Document()
    
    # Page setup - Margins
    for section in doc.sections:
        section.top_margin = Inches(0.8)
        section.bottom_margin = Inches(0.8)
        section.left_margin = Inches(0.8)
        section.right_margin = Inches(0.8)
        
        # Header & Footer setup
        header = section.header
        hp = header.paragraphs[0]
        hp.alignment = WD_ALIGN_PARAGRAPH.RIGHT
        hrun = hp.add_run("TP2 : Identifier ma cible d'entreprises | DSI 31")
        hrun.font.name = "Calibri"
        hrun.font.size = Pt(8.5)
        hrun.font.color.rgb = RGBColor(140, 150, 165)
        
        footer = section.footer
        fp = footer.paragraphs[0]
        fp.alignment = WD_ALIGN_PARAGRAPH.CENTER
        frun = fp.add_run("Page d'insertion et rapport de travaux pratiques - DSI 31 - Projet d'Intégration")
        frun.font.name = "Calibri"
        frun.font.size = Pt(8.5)
        frun.font.color.rgb = RGBColor(140, 150, 165)

    # PAGE DE GARDE / EN-TÊTE PRINCIPAL
    title_p = doc.add_paragraph()
    title_p.paragraph_format.space_before = Pt(20)
    title_p.paragraph_format.space_after = Pt(4)
    title_p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    trun = title_p.add_run("TRAVAUX PRATIQUES N° 2")
    trun.bold = True
    trun.font.name = "Calibri"
    trun.font.size = Pt(24)
    trun.font.color.rgb = RGBColor(15, 76, 129)

    sub_p = doc.add_paragraph()
    sub_p.paragraph_format.space_after = Pt(16)
    sub_p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    srun = sub_p.add_run("Module : Projet d'Intégration & Préparation Professionnelle\nThème : « Identifier ma cible d'entreprises (Opportunités d'embauche) »")
    srun.font.name = "Calibri"
    srun.font.size = Pt(14)
    srun.font.color.rgb = RGBColor(71, 85, 105)

    # Metadata Box
    meta_tbl = doc.add_table(rows=4, cols=2)
    meta_tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    meta_data = [
        ("Classe / Niveau :", "DSI 31 (Développement des Systèmes d'Information - 3ème Année)"),
        ("Entreprise Cible Principale :", "ZETABOX (Technologie & Développement Logiciel / IA)"),
        ("Poste Visé / Offre Ciblée :", "Développeur Front-End (Frontend Developer - React / TypeScript)"),
        ("Date de Réalisation :", "Novembre 2026")
    ]
    for idx, (label, val) in enumerate(meta_data):
        c0 = meta_tbl.cell(idx, 0)
        c1 = meta_tbl.cell(idx, 1)
        c0.width = Inches(2.2)
        c1.width = Inches(4.5)
        set_cell_background(c0, "F1F5F9")
        set_cell_background(c1, "FFFFFF")
        set_cell_margins(c0, 60, 60, 100, 100)
        set_cell_margins(c1, 60, 60, 100, 100)
        
        p0 = c0.paragraphs[0]
        r0 = p0.add_run(label)
        r0.bold = True
        r0.font.name = "Calibri"
        r0.font.size = Pt(9.5)
        r0.font.color.rgb = RGBColor(15, 76, 129)
        
        p1 = c1.paragraphs[0]
        r1 = p1.add_run(val)
        r1.font.name = "Calibri"
        r1.font.size = Pt(9.5)
        r1.font.color.rgb = RGBColor(30, 41, 59)

    doc.add_paragraph().paragraph_format.space_after = Pt(10)

    add_callout(
        doc,
        "Objectifs Pédagogiques du TP2",
        "1. Maîtriser les moteurs de recherche d'emploi et les stratégies de veille sur le marché du travail IT.\n"
        "2. Constituer un vivier qualifié de 100 entreprises technologiques et prioriser un Top 10 rigoureux.\n"
        "3. Sélectionner l'entreprise N°1 (ZetaBox) sur laquelle porteront toutes les étapes suivantes (CV, lettre, simulation d'entretien).\n"
        "4. Réaliser une fiche d'identité d'entreprise approfondie (produits, stack, politiques RH, rémunérations)."
    )

    doc.add_page_break()

    # SECTION 1 : EXERCICE 1
    h1 = doc.add_paragraph()
    h1.paragraph_format.space_before = Pt(12)
    h1.paragraph_format.space_after = Pt(6)
    r_h1 = h1.add_run("EXERCICE 1 : LISTE DES MOTEURS DE RECHERCHE DE JOB")
    r_h1.bold = True
    r_h1.font.name = "Calibri"
    r_h1.font.size = Pt(16)
    r_h1.font.color.rgb = RGBColor(15, 76, 129)

    p_intro = doc.add_paragraph()
    p_intro.paragraph_format.space_after = Pt(8)
    p_intro.add_run(
        "Pour identifier efficacement les opportunités d'embauche en développement informatique, "
        "la prospection doit s'appuyer sur une pluralité de canaux complémentaires : réseaux professionnels, "
        "moteurs généralistes, plateformes régionales (Tunisie) et portails spécialisés Tech / Télétravail."
    )

    # TABLE DES MOTEURS
    doc.add_paragraph().paragraph_format.space_after = Pt(2)
    p_tbl_title = doc.add_paragraph()
    r_tt = p_tbl_title.add_run("Tableau Synthétique des Moteurs & Plateformes de Recrutement")
    r_tt.bold = True
    r_tt.font.size = Pt(11)
    r_tt.font.color.rgb = RGBColor(15, 76, 129)

    tbl_engines = doc.add_table(rows=1, cols=5)
    headers = ["Catégorie", "Plateforme", "Lien Web", "Spécificités & Avantages", "Public & Pertinence IT"]
    for j, h in enumerate(headers):
        tbl_engines.cell(0, j).paragraphs[0].text = h
    
    engines_data = [
        ("Réseau Pro Global", "LinkedIn Jobs", "linkedin.com/jobs", "Ciblage ultra-précis par mots-clés (React, Frontend), contact direct avec les recruteurs/CTO, alertes d'offres instantanées.", "Indispensable (10/10)"),
        ("Agrégateur Global", "Indeed", "tn.indeed.com", "Volume massif d'annonces agrégées depuis les sites d'entreprises, filtres par type de contrat (CDI, SIVP, Stage).", "Très Élevé (8.5/10)"),
        ("Avis & Salaires", "Glassdoor", "glassdoor.com", "Annonces doublées d'avis anonymes d'employés, grilles de salaires réelles et questions posées en entretien d'embauche.", "Très Élevé (8.5/10)"),
        ("Moteur Web", "Google for Jobs", "google.com", "Moteur intégré indexant instantanément les offres du web entier sans intermédiaire.", "Élevé (8/10)"),
        ("Tunisie Spécifique", "Keejob", "keejob.com", "Leader du recrutement en Tunisie, grand vivier d'ESN et de startups tunisiennes recrutant des juniors.", "Indispensable Tunisie (9.5/10)"),
        ("Tunisie Spécifique", "Tanitjobs", "tanitjobs.com", "Nombreuses offres pour jeunes diplômés, stages PFE et premiers emplois en développement web.", "Élevé (8/10)"),
        ("Service Public TN", "Emploi.nat.tn (ANETI)", "emploi.nat.tn", "Portail officiel de l'ANETI pour les conventions de stages d'initiation (SIVP, Karama).", "Indispensable Contrats (8.5/10)"),
        ("Tech & Culture", "Welcome to the Jungle", "welcometothejungle.com", "Vitrines immersives d'entreprises tech, vidéos d'équipes, transparence sur la stack et les valeurs d'entreprise.", "Excellente Qualité (9/10)"),
        ("Télétravail Global", "We Work Remotely", "weworkremotely.com", "Leader mondial des offres de travail à distance (Full Remote) en développement Frontend / Fullstack.", "Très Bon pour Remote (8.5/10)"),
        ("Télétravail Tech", "RemoteOK", "remoteok.com", "Offres technologiques internationales 100% à distance avec affichage transparent des salaires en USD/EUR.", "Très Bon pour Freelance/Remote (8/10)")
    ]

    for row_data in engines_data:
        row = tbl_engines.add_row()
        for j, text in enumerate(row_data):
            row.cells[j].paragraphs[0].text = text

    format_table(tbl_engines, [1.3, 1.2, 1.4, 2.0, 1.1])

    # Astuces de recherche
    doc.add_paragraph().paragraph_format.space_after = Pt(4)
    add_callout(
        doc,
        "Techniques de Recherche Avancée (Google X-Ray & Opérateurs)",
        "• Requête booléenne ciblée : (\"Frontend Developer\" OR \"Développeur Front-End\") AND (React OR Next.js) AND (Tunisie OR Sfax OR Remote)\n"
        "• Google X-Ray Search : site:linkedin.com/jobs \"ZetaBox\" \"Frontend\"\n"
        "• Recherche de CV de recruteurs : site:linkedin.com/in/ (\"Tech Recruiter\" OR \"Talent Acquisition\") \"Tunisie\""
    )

    doc.add_page_break()

    # SECTION 2 : EXERCICE 2
    h2 = doc.add_paragraph()
    h2.paragraph_format.space_before = Pt(12)
    h2.paragraph_format.space_after = Pt(6)
    r_h2 = h2.add_run("EXERCICE 2 : STRATÉGIE DES 100 ENTREPRISES & CLASSEMENT TOP 10")
    r_h2.bold = True
    r_h2.font.name = "Calibri"
    r_h2.font.size = Pt(16)
    r_h2.font.color.rgb = RGBColor(15, 76, 129)

    p_e2 = doc.add_paragraph()
    p_e2.paragraph_format.space_after = Pt(8)
    p_e2.add_run(
        "Pour structurer une recherche d'emploi méthodique, une base de données de 100 entreprises cibles a été compilée dans un tableau Excel. "
        "Les entreprises ont été sélectionnées selon une typologie équilibrée : 40% ESN / Sociétés de services IT, 25% Éditeurs de logiciels SaaS, "
        "20% Startups innovantes (IA/Web3/Fintech) et 15% Agences digitales et studios de développement."
    )

    p_crit = doc.add_paragraph()
    p_crit.paragraph_format.space_after = Pt(6)
    r_cr = p_crit.add_run("Grille d'Évaluation & Critères de Pondération du Top 10 :")
    r_cr.bold = True
    r_cr.font.color.rgb = RGBColor(15, 76, 129)

    bullets = [
        "Adéquation de la Stack Frontend (Note /5) : Utilisation de technologies modernes et recherchées (React.js, Next.js, TypeScript, TailwindCSS).",
        "Environnement d'Apprentissage & Mentorat (Note /4) : Encadrement par des Leads seniors, revues de code, veille technologique, projets stimulants.",
        "Flexibilité & Cadre de Travail (Note /4) : Politique de télétravail (Remote / Hybride), équilibre vie pro/perso, ambiance jeune et dynamique.",
        "Attractivité Salariale & Évolution (Note /4) : Rémunération compétitive en Tunisie, primes, perspectives d'embauche post-SIVP.",
        "Solidité & Réputation de l'Entreprise (Note /3) : Notoriété sur le marché, santé financière, portefeuille de clients internationaux."
    ]
    for b in bullets:
        bp = doc.add_paragraph(b, style='List Bullet')
        bp.paragraph_format.space_after = Pt(3)

    # TABLE TOP 10
    doc.add_paragraph().paragraph_format.space_after = Pt(4)
    p_t10 = doc.add_paragraph()
    r_t10 = p_t10.add_run("Classement Officiel du Top 10 des Entreprises Cibles")
    r_t10.bold = True
    r_t10.font.size = Pt(11)
    r_t10.font.color.rgb = RGBColor(15, 76, 129)

    tbl_top10 = doc.add_table(rows=1, cols=6)
    top_headers = ["Rang", "Entreprise", "Secteur", "Localisation", "Stack Frontend Clé", "Note /20"]
    for j, h in enumerate(top_headers):
        tbl_top10.cell(0, j).paragraphs[0].text = h

    top10_data = [
        ("N° 1", "ZetaBox", "Dev Logiciel & Solutions IA", "Sfax / Remote", "React, Next.js, Vue.js, TypeScript", "19.0 / 20 (Choix N°1)"),
        ("N° 2", "InstaDeep (BioNTech)", "Intelligence Artificielle Avancée", "Tunis / Remote", "React, TypeScript, D3.js", "18.5 / 20"),
        ("N° 3", "Expensya (Medius)", "Fintech SaaS & Gestion B2B", "Tunis (Lac 2)", "Angular, React, TypeScript", "18.0 / 20"),
        ("N° 4", "Vermeg", "Éditeur Logiciel Banque/Assurance", "Tunis (Lac / Mégrine)", "Angular, React, Micro-frontends", "17.5 / 20"),
        ("N° 5", "Sofrecom Tunisie (Orange)", "ESN Télécoms & Digital", "Tunis (Charguia)", "Angular, React.js, Vue.js", "17.0 / 20"),
        ("N° 6", "Talan Tunisie", "Conseil & Services Numériques", "Tunis (Charguia)", "React, Angular, Vue, Flutter", "16.5 / 20"),
        ("N° 7", "Think-iT", "Ingénierie Logicielle Internationale", "Tunis / Full Remote", "React, TypeScript, GraphQL", "16.5 / 20"),
        ("N° 8", "Wevioo", "Conseil & Transformation Digitale", "Tunis (Ariana)", "React, Angular, TypeScript", "16.0 / 20"),
        ("N° 9", "Vneuron", "Regtech & Logiciels Bancaires", "Tunis (El Ghazala)", "Angular, React, TailwindCSS", "15.5 / 20"),
        ("N° 10", "Obytes", "Développement Web & Mobile Agile", "Tunis / Remote", "React, Next.js, React Native", "15.0 / 20")
    ]

    for row_data in top10_data:
        row = tbl_top10.add_row()
        for j, text in enumerate(row_data):
            row.cells[j].paragraphs[0].text = text

    format_table(tbl_top10, [0.8, 1.4, 1.6, 1.2, 1.4, 1.1])

    doc.add_paragraph().paragraph_format.space_after = Pt(6)
    add_callout(
        doc,
        "Justification du Choix de la 1ère Entreprise : ZETABOX (Offre Frontend Developer)",
        "Conformément à la consigne « on va travailler sur la 1ère jusqu'à la fin », ZETABOX a été désignée comme notre cible d'excellence :\n"
        "1. Alignement parfait des technologies : ZetaBox utilise exactement l'écosystème web moderne (React.js, Next.js, TypeScript, TailwindCSS) enseigné et pratiqué en DSI31.\n"
        "2. Dimensionnement idéal : PME technologique à taille humaine permettant une forte responsabilité, une visibilité directe et une progression rapide sans lourdeurs administratives.\n"
        "3. Positionnement avant-gardiste sur l'IA : Intégration de solutions d'IA et de modernisation logicielle, offrant un tremplin de carrière exceptionnel."
    )

    doc.add_page_break()

    # SECTION 3 : ESPACES CAPTURES D'ÉCRAN DU TOP 10
    h_screens = doc.add_paragraph()
    h_screens.paragraph_format.space_before = Pt(12)
    h_screens.paragraph_format.space_after = Pt(6)
    r_hs = h_screens.add_run("ANNEXE : FICHES ET EMPLACEMENTS DES CAPTURES D'ÉCRAN (TOP 10)")
    r_hs.bold = True
    r_hs.font.name = "Calibri"
    r_hs.font.size = Pt(15)
    r_hs.font.color.rgb = RGBColor(15, 76, 129)

    p_sc_desc = doc.add_paragraph()
    p_sc_desc.paragraph_format.space_after = Pt(8)
    p_sc_desc.add_run(
        "Cette section présente pour chaque entreprise du Top 10 l'URL officielle, l'offre d'emploi ciblée "
        "et les cadres réservés pour insérer les captures d'écran justificatives (Page d'accueil/carrières et fiche de poste)."
    )

    screen_targets = [
        ("1. ZETABOX (Choix Principal)", "https://zeta-box.com", "https://zeta-box.com/careers", "Frontend Developer (Full-time / Remote & Sfax)", "ReactJS, Next.js, TypeScript, REST API, Git"),
        ("2. InstaDeep (BioNTech)", "https://www.instadeep.com", "https://www.instadeep.com/careers/", "Frontend Software Engineer (AI Products)", "React, TypeScript, Data Visualization, D3.js"),
        ("3. Expensya (Medius)", "https://www.expensya.com", "https://www.expensya.com/fr/carrieres/", "Frontend Engineer / Développeur Web", "Angular, TypeScript, State Management, APIs"),
        ("4. Vermeg", "https://www.vermeg.com", "https://www.vermeg.com/careers/", "Ingénieur Développement Frontend", "Angular, React, Micro-frontends, Java/Spring backend"),
        ("5. Sofrecom Tunisie", "https://www.sofrecom.com", "https://www.sofrecom.com/carrieres", "Développeur Web Frontend Confirmé / Junior", "React.js, Vue.js, Angular, Méthodologie Agile"),
        ("6. Talan Tunisie", "https://talan.com", "https://talan.com/rejoignez-nous/", "Consultant Développeur Frontend", "React, Angular, TypeScript, UI/UX Integration"),
        ("7. Think-iT", "https://think-it.io", "https://think-it.io/careers/", "Frontend Software Engineer", "TypeScript, React, Node.js, GraphQL"),
        ("8. Wevioo", "https://www.wevioo.com", "https://www.wevioo.com/fr/carrieres", "Ingénieur d'Études et Développement Web Frontend", "Angular, React, REST Services, Git"),
        ("9. Vneuron", "https://www.vneuron.com", "https://www.vneuron.com/careers/", "Frontend Developer (SparkSuite)", "Angular, React, CSS3, TailwindCSS, REST APIs"),
        ("10. Obytes", "https://obytes.com", "https://obytes.com/careers", "React / Frontend Developer", "React, Next.js, TypeScript, React Native")
    ]

    for title, site, careers, role, stack in screen_targets:
        box_tbl = doc.add_table(rows=2, cols=1)
        box_tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
        c_top = box_tbl.cell(0, 0)
        c_bot = box_tbl.cell(1, 0)
        c_top.width = Inches(6.8)
        c_bot.width = Inches(6.8)
        
        set_cell_background(c_top, "0F4C81")
        set_cell_background(c_bot, "FAFAFA")
        set_cell_margins(c_top, 80, 80, 140, 140)
        set_cell_margins(c_bot, 120, 120, 140, 140)
        
        pt = c_top.paragraphs[0]
        rt = pt.add_run(f"🏢 {title}")
        rt.bold = True
        rt.font.color.rgb = RGBColor(255, 255, 255)
        rt.font.size = Pt(10.5)
        
        pb = c_bot.paragraphs[0]
        pb.paragraph_format.space_after = Pt(2)
        rb = pb.add_run(f"• Site Web : {site} | Page Carrières : {careers}\n"
                        f"• Offre Ciblée : {role}\n"
                        f"• Stack Technique : {stack}\n"
                        f"📷 [Emplacement réservé : Insérer la capture d'écran du site et de l'offre d'emploi ici]")
        rb.font.size = Pt(9)
        rb.font.color.rgb = RGBColor(71, 85, 105)
        
        doc.add_paragraph().paragraph_format.space_after = Pt(2)

    doc.add_page_break()

    # SECTION 4 : KIT - FICHE D'IDENTITÉ ZETABOX
    h_kit = doc.add_paragraph()
    h_kit.paragraph_format.space_before = Pt(12)
    h_kit.paragraph_format.space_after = Pt(6)
    r_hk = h_kit.add_run("KIT : FICHE D'IDENTITÉ APPROFONDIE – ZETABOX")
    r_hk.bold = True
    r_hk.font.name = "Calibri"
    r_hk.font.size = Pt(16)
    r_hk.font.color.rgb = RGBColor(15, 76, 129)

    add_callout(
        doc,
        "Présentation Synthétique de l'Entreprise Cible",
        "ZetaBox est une société technologique spécialisée dans le développement de logiciels sur mesure, "
        "l'externalisation informatique (IT Outsourcing) et l'intégration de solutions avancées d'Intelligence Artificielle. "
        "Elle conçoit des plateformes web complexes et accompagne des clients internationaux dans leur modernisation applicative."
    )

    # 1. CARTE D'IDENTITÉ
    h_k1 = doc.add_paragraph()
    h_k1.paragraph_format.space_before = Pt(8)
    h_k1.paragraph_format.space_after = Pt(4)
    r_k1 = h_k1.add_run("1. Données d'Identification de l'Entreprise")
    r_k1.bold = True
    r_k1.font.size = Pt(12)
    r_k1.font.color.rgb = RGBColor(15, 76, 129)

    id_tbl = doc.add_table(rows=7, cols=2)
    id_info = [
        ("Dénomination Commerciale / Raison Sociale", "ZetaBox (ZetaBox Technologies)"),
        ("Site Web Officiel & URL Carrières", "https://zeta-box.com | https://zeta-box.com/careers"),
        ("Siège Social & Localisation", "Avenue Majida Boulila, Sfax, Tunisie"),
        ("Mode de Présence", "Bureaux à Sfax + Télétravail (Modèle Hybride / Remote-friendly)"),
        ("Courriel & Contact Recrutement", "contact@zeta-box.com / Formulaire carrières en ligne"),
        ("Secteur d'Activité / Métier", "Développement Logiciel, IT Outsourcing, Intégration IA"),
        ("Taille de la Structure & Effectif", "PME en forte croissance (~20 à 50+ collaborateurs)")
    ]
    for idx, (lbl, val) in enumerate(id_info):
        c0 = id_tbl.cell(idx, 0)
        c1 = id_tbl.cell(idx, 1)
        c0.width = Inches(2.5)
        c1.width = Inches(4.3)
        set_cell_background(c0, "F8FAFC")
        set_cell_margins(c0, 60, 60, 100, 100)
        set_cell_margins(c1, 60, 60, 100, 100)
        p0 = c0.paragraphs[0]
        r0 = p0.add_run(lbl)
        r0.bold = True
        r0.font.size = Pt(9.5)
        r0.font.color.rgb = RGBColor(15, 76, 129)
        p1 = c1.paragraphs[0]
        r1 = p1.add_run(val)
        r1.font.size = Pt(9.5)
        r1.font.color.rgb = RGBColor(30, 41, 59)

    format_table(id_tbl, [2.5, 4.3])

    # 2. PRODUITS ET SERVICES
    h_k2 = doc.add_paragraph()
    h_k2.paragraph_format.space_before = Pt(10)
    h_k2.paragraph_format.space_after = Pt(4)
    r_k2 = h_k2.add_run("2. Offres de Produits, Services & Métiers")
    r_k2.bold = True
    r_k2.font.size = Pt(12)
    r_k2.font.color.rgb = RGBColor(15, 76, 129)

    services = [
        ("Développement Logiciel Web & Mobile sur mesure : ", "Création d'applications complètes pour des clients européens et internationaux (portails SaaS, dashboards interactifs, back-offices complexes, applications mobiles cross-platform)."),
        ("Solutions d'Intelligence Artificielle & Agents Autonomes : ", "Conception et déploiement d'agents IA conversationnels, automatisation de workflows métiers, intégration de modèles de fondation (Anthropic Claude, OpenAI GPT) dans des logiciels métier."),
        ("Modernisation de Systèmes Legacy : ", "Refactorisation de codes et migration de systèmes anciens vers des architectures modernes (React, Next.js, APIs RESTful, architectures modulaires)."),
        ("Équipes Dédiées en IT Outsourcing : ", "Mise en place d'escouades d'ingénieurs qualifiés pour accélérer la livraison de projets clients avec application stricte des méthodologies agiles (Scrum/Kanban).")
    ]
    for stitle, sdesc in services:
        sp = doc.add_paragraph(style='List Bullet')
        sp.paragraph_format.space_after = Pt(3)
        r_st = sp.add_run(stitle)
        r_st.bold = True
        r_st.font.color.rgb = RGBColor(15, 76, 129)
        r_sd = sp.add_run(sdesc)
        r_sd.font.color.rgb = RGBColor(45, 55, 72)

    # 3. STACK TECHNIQUE FRONTEND
    h_k3 = doc.add_paragraph()
    h_k3.paragraph_format.space_before = Pt(10)
    h_k3.paragraph_format.space_after = Pt(4)
    r_k3 = h_k3.add_run("3. Stack Technologique & Écosystème Frontend chez ZetaBox")
    r_k3.bold = True
    r_k3.font.size = Pt(12)
    r_k3.font.color.rgb = RGBColor(15, 76, 129)

    stack_tbl = doc.add_table(rows=1, cols=3)
    stack_headers = ["Composante Technique", "Technologies Utilisées", "Rôle & Utilité dans les Projets"]
    for j, h in enumerate(stack_headers):
        stack_tbl.cell(0, j).paragraphs[0].text = h

    stack_data = [
        ("Frameworks Web", "ReactJS, Next.js, Vue.js, Nuxt.js, Angular", "Développement d'interfaces modulaires, Single Page Applications (SPA) et Server-Side Rendering (SSR)."),
        ("Langages Core", "TypeScript, JavaScript (ES6+), HTML5/CSS3", "Typage statique robuste pour prévenir les bugs en production et assurer une maintenance fluide."),
        ("Styling & Design System", "Tailwind CSS, SASS/SCSS, Material UI, Shadcn UI", "Conception d'interfaces modernes, conformes aux maquettes Figma et hautement responsives."),
        ("Gestion d'État (State)", "Redux Toolkit, Context API, Zustand", "Centralisation et synchronisation prédictive des données complexes dans les applications."),
        ("Communication & API", "REST APIs, GraphQL, Axios, TanStack Query", "Consommation asynchrone sécurisée des endpoints fournis par l'équipe backend."),
        ("Outils & DevOps", "Git, GitHub, Docker, Vite, Jest, CI/CD", "Gestion des versions de code, revue par les pairs (PRs) et déploiements automatisés.")
    ]
    for row_data in stack_data:
        row = stack_tbl.add_row()
        for j, text in enumerate(row_data):
            row.cells[j].paragraphs[0].text = text

    format_table(stack_tbl, [1.8, 2.2, 2.8])

    # 4. ANALYSE DU POSTE DE FRONTEND DEVELOPER
    h_k4 = doc.add_paragraph()
    h_k4.paragraph_format.space_before = Pt(10)
    h_k4.paragraph_format.space_after = Pt(4)
    r_k4 = h_k4.add_run("4. Analyse Détaillée de l'Offre Ciblée : Frontend Developer")
    r_k4.bold = True
    r_k4.font.size = Pt(12)
    r_k4.font.color.rgb = RGBColor(15, 76, 129)

    p_poste = doc.add_paragraph()
    p_poste.paragraph_format.space_after = Pt(4)
    p_poste.add_run(
        "L'offre de Développeur Front-End chez ZetaBox s'adresse à un profil passionné par la création d'expériences web interactives. "
        "Le candidat rejoint une équipe pluridisciplinaire (UX designers, développeurs backend, testeurs QA)."
    )

    missions = [
        "Développement et maintenance d'applications web réactives en utilisant les frameworks JavaScript/TypeScript modernes.",
        "Collaboration étroite avec les designers UI/UX pour implémenter fidèlement les maquettes et animations.",
        "Optimisation des temps de chargement, des performances web et respect des standards SEO et d'accessibilité.",
        "Rédaction de code propre, testé, bien documenté et participation active aux revues de code (Code Reviews).",
        "Résolution des bugs techniques et veille constante sur les nouveautés des bibliothèques Frontend."
    ]
    for m in missions:
        mp = doc.add_paragraph(f"• {m}", style='List Bullet')
        mp.paragraph_format.space_after = Pt(2)

    # 5. POLITIQUES RH, SALAIRES ET AVANTAGES
    h_k5 = doc.add_paragraph()
    h_k5.paragraph_format.space_before = Pt(10)
    h_k5.paragraph_format.space_after = Pt(4)
    r_k5 = h_k5.add_run("5. Politiques RH, Salaires & Avantages Sociaux")
    r_k5.bold = True
    r_k5.font.size = Pt(12)
    r_k5.font.color.rgb = RGBColor(15, 76, 129)

    add_callout(
        doc,
        "Grille Salariale Estimative pour Développeur Frontend (Marché Tunisien / ZetaBox)",
        "• Profil Débutant / Premier Emploi (Sortant Licence DSI / 0 à 2 ans) : 1 200 DT à 1 800 DT Net / mois.\n"
        "  -> Formule habituelle d'intégration : Contrat SIVP (exonération fiscale) avec prime d'embauche versée par l'entreprise, avec titularisation en CDI.\n"
        "• Profil Confirmé (2 à 4 ans d'expérience) : 2 000 DT à 3 200+ DT Net / mois selon compétences techniques et autonomie.\n"
        "• Avantages Sociaux & Cadre de Travail : Tickets restaurant (ou prime panier), assurance groupe maladie, PC portable performant, "
        "horaires flexibles, possibilité de travail en Remote et événements d'équipe."
    )

    # 6. PROCESSUS DE RECRUTEMENT
    h_k6 = doc.add_paragraph()
    h_k6.paragraph_format.space_before = Pt(10)
    h_k6.paragraph_format.space_after = Pt(4)
    r_k6 = h_k6.add_run("6. Processus de Recrutement Typique chez ZetaBox")
    r_k6.bold = True
    r_k6.font.size = Pt(12)
    r_k6.font.color.rgb = RGBColor(15, 76, 129)

    steps = [
        ("Étape 1 : Présélection sur Dossier", "Examen du CV, du compte GitHub et des réalisations antérieures (projets d'intégration, projets personnels en React)."),
        ("Étape 2 : Entretien RH / Culture Fit (20-30 min)", "Échange portant sur le parcours, la motivation, la maîtrise du français et de l'anglais technique, et l'adéquation avec la culture de l'entreprise."),
        ("Étape 3 : Évaluation Technique (Coding Challenge)", "Réalisation d'un mini-projet Frontend (ex: dashboard consommant une API avec composants réutilisables) ou test de live-coding."),
        ("Étape 4 : Entretien Technique d'Architecture & Décision", "Débriefing avec le Lead Developer ou le CTO sur les choix d'implémentation, suivi de la proposition formelle d'embauche.")
    ]
    for step_num, step_desc in steps:
        sp = doc.add_paragraph()
        sp.paragraph_format.space_after = Pt(3)
        r_sn = sp.add_run(f"✓ {step_num} : ")
        r_sn.bold = True
        r_sn.font.color.rgb = RGBColor(15, 76, 129)
        r_sd = sp.add_run(step_desc)
        r_sd.font.color.rgb = RGBColor(45, 55, 72)

    # 7. CONCLUSION ET STRATÉGIE
    h_k7 = doc.add_paragraph()
    h_k7.paragraph_format.space_before = Pt(10)
    h_k7.paragraph_format.space_after = Pt(4)
    r_k7 = h_k7.add_run("7. Stratégie pour les TP Suivants (Travail jusqu'à la fin)")
    r_k7.bold = True
    r_k7.font.size = Pt(12)
    r_k7.font.color.rgb = RGBColor(15, 76, 129)

    p_strat = doc.add_paragraph()
    p_strat.paragraph_format.space_after = Pt(6)
    p_strat.add_run(
        "Puisque la consigne stipule « on va travailler sur la 1ère jusqu'à la fin », toutes les livraisons ultérieures "
        "du module seront calibrées spécifiquement pour ZETABOX :\n"
        "• Rédaction d'un CV Technique percutant mettant en valeur les projets React, Next.js et TypeScript réalisés en DSI31.\n"
        "• Conception d'une Lettre de Motivation sur-mesure démontrant une compréhension fine des activités de ZetaBox dans le développement web et l'IA.\n"
        "• Préparation aux questions d'entretien technique typiques de ZetaBox (Cycle de vie React, Hooks, gestion d'état, performances)."
    )

    # Save DOCX
    docx_path = os.path.abspath("TP2_Opportunites_Embauche_ZetaBox.docx")
    pdf_path = os.path.abspath("TP2_Opportunites_Embauche_ZetaBox.pdf")
    doc.save(docx_path)
    print("DOCX successfully generated at:", docx_path)

    # Convert to PDF via Word COM
    word = win32com.client.Dispatch("Word.Application")
    word.Visible = False
    try:
        doc_word = word.Documents.Open(docx_path)
        doc_word.SaveAs(pdf_path, FileFormat=17) # 17 = wdFormatPDF
        doc_word.Close()
        print("PDF successfully generated at:", pdf_path)
    except Exception as e:
        print("Error during PDF conversion:", e)
    finally:
        word.Quit()

if __name__ == "__main__":
    build_tp2_document()
