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

def set_cell_margins(cell, top=100, bottom=100, left=140, right=140):
    tcPr = cell._tc.get_or_add_tcPr()
    tcMar = OxmlElement('w:tcMar')
    for m, val in [('top', top), ('bottom', bottom), ('left', left), ('right', right)]:
        node = OxmlElement(f'w:{m}')
        node.set(qn('w:w'), str(val))
        node.set(qn('w:type'), 'dxa')
        tcMar.append(node)
    tcPr.append(tcMar)

def add_callout(doc, title, text, bg_color="F1F5F9", border_color="0F4C81"):
    tbl = doc.add_table(rows=1, cols=1)
    tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    cell = tbl.cell(0, 0)
    set_cell_background(cell, bg_color)
    set_cell_margins(cell, top=140, bottom=140, left=200, right=180)
    
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
    p.paragraph_format.space_after = Pt(3)
    run_t = p.add_run(f"📌 {title}\n")
    run_t.bold = True
    run_t.font.name = "Calibri"
    run_t.font.size = Pt(11)
    run_t.font.color.rgb = RGBColor(15, 76, 129)
    
    run_b = p.add_run(text)
    run_b.font.name = "Calibri"
    run_b.font.size = Pt(9.5)
    run_b.font.color.rgb = RGBColor(30, 41, 59)
    doc.add_paragraph().paragraph_format.space_after = Pt(2)

def format_table(tbl, col_widths=None, header_bg="0F4C81", alt_bg="F8FAFC"):
    tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    for i, row in enumerate(tbl.rows):
        is_header = (i == 0)
        for j, cell in enumerate(row.cells):
            set_cell_margins(cell, top=100, bottom=100, left=130, right=130)
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
            
            tcPr = cell._tc.get_or_add_tcPr()
            borders = parse_xml(f'''
                <w:tcBorders {nsdecls("w")}>
                    <w:top w:val="single" w:sz="4" w:space="0" w:color="CBD5E1"/>
                    <w:left w:val="single" w:sz="4" w:space="0" w:color="CBD5E1"/>
                    <w:bottom w:val="single" w:sz="4" w:space="0" w:color="CBD5E1"/>
                    <w:right w:val="single" w:sz="4" w:space="0" w:color="CBD5E1"/>
                </w:tcBorders>
            ''')
            tcPr.append(borders)

def build_tp4_document():
    doc = docx.Document()
    
    # Page setup - Margins
    for section in doc.sections:
        section.top_margin = Inches(0.8)
        section.bottom_margin = Inches(0.8)
        section.left_margin = Inches(0.8)
        section.right_margin = Inches(0.8)
        
        # Header
        header = section.header
        hp = header.paragraphs[0]
        hp.alignment = WD_ALIGN_PARAGRAPH.RIGHT
        hrun = hp.add_run("TP4 : Préparation à l’entretien – ZetaBox | Iheb Najjar - DSI 31")
        hrun.font.name = "Calibri"
        hrun.font.size = Pt(8.5)
        hrun.font.color.rgb = RGBColor(140, 150, 165)
        
        # Footer
        footer = section.footer
        fp = footer.paragraphs[0]
        fp.alignment = WD_ALIGN_PARAGRAPH.CENTER
        frun = fp.add_run("ISET Nabeul · Département Technologies de l'Informatique · S6 (21)")
        frun.font.name = "Calibri"
        frun.font.size = Pt(8.5)
        frun.font.color.rgb = RGBColor(140, 150, 165)

    # TITRE PRINCIPAL
    title_p = doc.add_paragraph()
    title_p.paragraph_format.space_before = Pt(10)
    title_p.paragraph_format.space_after = Pt(2)
    title_p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    trun = title_p.add_run("S6 (21) · TRAVAUX PRATIQUES N° 4")
    trun.bold = True
    trun.font.name = "Calibri"
    trun.font.size = Pt(22)
    trun.font.color.rgb = RGBColor(15, 76, 129)

    sub_p = doc.add_paragraph()
    sub_p.paragraph_format.space_after = Pt(14)
    sub_p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    srun = sub_p.add_run("Préparation à l’Entretien d’Embauche : Entreprise ZETABOX\nPoste Visé : Développeur Front-End (Frontend Developer)")
    srun.font.name = "Calibri"
    srun.font.size = Pt(13)
    srun.font.color.rgb = RGBColor(51, 65, 85)

    # CARTUCHE D'IDENTITÉ
    meta_tbl = doc.add_table(rows=4, cols=2)
    meta_tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    meta_data = [
        ("Candidat :", "Iheb Najjar (Étudiant en 3e année DSI)"),
        ("Établissement :", "Institut Supérieur des Études Technologiques de Nabeul (ISET Nabeul)"),
        ("Entreprise Cible :", "ZetaBox (Sfax / Télétravail - Développement Logiciel & Solutions IA)"),
        ("Profil Technique :", "Full-Stack orienté Frontend (React.js, Next.js, TypeScript, TailwindCSS)")
    ]
    for idx, (label, val) in enumerate(meta_data):
        c0 = meta_tbl.cell(idx, 0)
        c1 = meta_tbl.cell(idx, 1)
        c0.width = Inches(2.2)
        c1.width = Inches(4.5)
        set_cell_background(c0, "F1F5F9")
        set_cell_background(c1, "FFFFFF")
        set_cell_margins(c0, 50, 50, 90, 90)
        set_cell_margins(c1, 50, 50, 90, 90)
        
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

    doc.add_paragraph().paragraph_format.space_after = Pt(8)

    # SECTION 1 : QUESTIONS / RÉPONSES PRÉPARÉES
    h1 = doc.add_paragraph()
    h1.paragraph_format.space_before = Pt(8)
    h1.paragraph_format.space_after = Pt(6)
    r_h1 = h1.add_run("1. QUESTIONS FRÉQUENTES & RÉPONSES PRÉPARÉES")
    r_h1.bold = True
    r_h1.font.name = "Calibri"
    r_h1.font.size = Pt(15)
    r_h1.font.color.rgb = RGBColor(15, 76, 129)

    qa_list = [
        (
            "1. Présentez-vous.",
            "« Je suis Iheb Najjar, étudiant en 3e année DSI à l’ISET de Nabeul, développeur Full-Stack orienté frontend (React, Next.js, TypeScript). J’ai conçu une application de gestion des formations chez Tunisie Telecom et la plateforme MediRDV. Organisé et orienté solutions, je cherche mon premier poste de développeur frontend. »",
            "Mettre l'accent sur les projets réels (Tunisie Telecom, MediRDV) et le positionnement clair sur la stack moderne React."
        ),
        (
            "2. Pourquoi ZetaBox ?",
            "« ZetaBox développe des logiciels sur mesure avec une stack moderne (front, back, mobile, IA, cloud). Ses projets clients variés me permettent de mettre en pratique mes compétences React et Next.js. »",
            "Démontrer la connaissance des activités de ZetaBox (sur-mesure, cloud, intégration IA) et la concordance avec ses compétences."
        ),
        (
            "3. Force et faiblesse ?",
            "« Ma force : l’organisation et la planification. Ma faiblesse : je voulais tout gérer seul ; j’ai corrigé cela avec Git, les revues de code et la répartition des tâches. »",
            "La faiblesse est constructive et montre une maturité professionnelle acquise grâce au travail collaboratif et à Git."
        ),
        (
            "4. Moins de 3 ans d’expérience ?",
            "« L’offre demande plus de 3 ans avec React. C’est vrai, mais j’ai deux stages, plus de dix projets publiés et le cours React Coursera. J’apprends vite et j’accepte l’encadrement. »",
            "Transformer le manque d'années formelles en preuve d'investissement concret : projets concrets en ligne, certification et adaptabilité."
        ),
        (
            "5. Vos prétentions salariales ?",
            "« D’après mes recherches, un développeur frontend junior (0 à 2 ans) gagne entre 1 200 et 1 900 TND par mois. Vu mes projets et mon profil, je vise 1 800 TND net par mois, et je reste ouvert à discuter selon les missions et les avantages. »",
            "Basé sur l'étude de marché du TP2, posture argumentée et ouverte au dialogue (fourchette + ciblage à 1 800 TND)."
        ),
        (
            "6. Quand pouvez-vous commencer ?",
            "« Je suis disponible immédiatement à l'issue de mes cours et examens universitaires (DSI 3). »",
            "Clarté sur la disponibilité et cohérence avec le calendrier académique de l'ISET."
        ),
        (
            "7. Et en dehors du travail ?",
            "« Je développe mes projets personnels (CaptionStudio, VoiceWave Studio) et j’aime maintenir une pratique sportive et des activités créatives. Cela me garde créatif, discipliné et curieux. »",
            "Montre la passion pour le développement même hors cadre scolaire et un bon équilibre de vie."
        ),
        (
            "8. Mes questions à poser au recruteur / Lead tech ?",
            "« Quels sont les projets clients actuels pour l’équipe frontend ? Comment se déroulent les revues de code au quotidien ? Un profil junior encadré est-il envisageable au sein de l'équipe, et le poste est-il en télétravail ou basé à Sfax ? »",
            "Prouve l'intérêt pour l'organisation de l'équipe technique et la volonté de s'intégrer sereinement."
        )
    ]

    for q, a, note in qa_list:
        qa_tbl = doc.add_table(rows=2, cols=1)
        qa_tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
        c_q = qa_tbl.cell(0, 0)
        c_a = qa_tbl.cell(1, 0)
        c_q.width = Inches(6.8)
        c_a.width = Inches(6.8)
        
        set_cell_background(c_q, "0F4C81")
        set_cell_background(c_a, "F8FAFC")
        set_cell_margins(c_q, 60, 60, 120, 120)
        set_cell_margins(c_a, 100, 100, 120, 120)
        
        pq = c_q.paragraphs[0]
        rq = pq.add_run(f"❓ {q}")
        rq.bold = True
        rq.font.color.rgb = RGBColor(255, 255, 255)
        rq.font.size = Pt(10)
        
        pa = c_a.paragraphs[0]
        pa.paragraph_format.space_after = Pt(2)
        ra = pa.add_run(f"Réponse préparée :\n{a}\n\n")
        ra.font.size = Pt(9.5)
        ra.font.color.rgb = RGBColor(30, 41, 59)
        
        rn = pa.add_run(f"💡 Conseil clé : {note}")
        rn.font.size = Pt(8.5)
        rn.font.italic = True
        rn.font.color.rgb = RGBColor(100, 116, 139)
        
        doc.add_paragraph().paragraph_format.space_after = Pt(2)

    doc.add_page_break()

    # SECTION 2 : STRATÉGIE SALARIALE
    h2 = doc.add_paragraph()
    h2.paragraph_format.space_before = Pt(8)
    h2.paragraph_format.space_after = Pt(6)
    r_h2 = h2.add_run("2. STRATÉGIE DE NÉGOCIATION SALARIALE (À RETENIR)")
    r_h2.bold = True
    r_h2.font.name = "Calibri"
    r_h2.font.size = Pt(15)
    r_h2.font.color.rgb = RGBColor(15, 76, 129)

    add_callout(
        doc,
        "Principes Fondamentaux de la Négociation Salariale (ZetaBox / Marché Tunisien)",
        "1. Clarification Brut vs Net : Toujours demander si le montant discuté est exprimé en Brut ou en Net, car les charges sociales et régimes fiscaux (ex: contrat SIVP sans impôt) font varier le montant perçu.\n"
        "2. Proposition par Fourchette : Ne jamais donner un chiffre fixe unique au début. Présenter une fourchette réaliste (1 600 - 1 900 TND) et maintenir sa valeur sans baisser immédiatement.\n"
        "3. Définition du Salaire Plancher : Fixer mon seuil minimum absolu à 1 500 TND Net/mois en dessous duquel je ne m'engage pas.\n"
        "4. Salaire Cible Visé : 1 800 TND Net/mois, justifié par mes réalisations concrètes (Tunisie Telecom, MediRDV, certification React)."
    )

    sal_tbl = doc.add_table(rows=1, cols=3)
    sal_headers = ["Niveau / Définition", "Montant (TND Net)", "Contexte & Justification"]
    for j, h in enumerate(sal_headers):
        sal_tbl.cell(0, j).paragraphs[0].text = h

    sal_rows = [
        ("Salaire Minimum Acceptable (Plancher)", "1 500 TND", "Seuil de sécurité garantissant une indépendance financière minimale pour un poste junior."),
        ("Salaire Cible Visé (Prétention)", "1 800 TND", "Montant aligné avec mes 10+ projets publiés, mes 2 stages réussis et la maîtrise de Next.js."),
        ("Fourchette de Référence Marché IT", "1 200 à 1 900 TND", "Données issues de l'étude de marché TP2 pour les profils sortants de licence DSI en Tunisie.")
    ]
    for r in sal_rows:
        row = sal_tbl.add_row()
        for j, val in enumerate(r):
            row.cells[j].paragraphs[0].text = val
    format_table(sal_tbl, [2.2, 1.6, 3.0])

    doc.add_paragraph().paragraph_format.space_after = Pt(10)

    # SECTION 3 : RÉVISION TECHNIQUE À LA MAIN
    h3 = doc.add_paragraph()
    h3.paragraph_format.space_before = Pt(8)
    h3.paragraph_format.space_after = Pt(6)
    r_h3 = h3.add_run("3. FICHES MÉMO : RÉVISION TECHNIQUE (À LA MAIN)")
    r_h3.bold = True
    r_h3.font.name = "Calibri"
    r_h3.font.size = Pt(15)
    r_h3.font.color.rgb = RGBColor(15, 76, 129)

    p_rev = doc.add_paragraph()
    p_rev.paragraph_format.space_after = Pt(6)
    p_rev.add_run(
        "Ce mémo technique récapitule les concepts clés à maîtriser pour le test technique et l'entretien avec le CTO / Lead Developer de ZetaBox :"
    )

    tech_topics = [
        (
            "Gestion d'État : Redux vs Context API",
            "• Context API : Intégré à React, idéal pour des états globaux légers à mise à jour peu fréquente (thème, utilisateur authentifié, langue).\n"
            "• Redux (Toolkit) : Préférable pour les applications d'entreprise volumineuses avec états partagés complexes, middleware (Redux Thunk/Saga), devtools avancés et logique découplée."
        ),
        (
            "Styling & CSS Avancé : Sass & Préprocesseurs",
            "• Variables, nesting (imbrication de sélecteurs), mixins réutilisables, héritage (@extend) et fonctions.\n"
            "• Organisation modulaire (architecture BEM, CSS Modules, TailwindCSS) pour éviter les collisions de styles globaux."
        ),
        (
            "Rendu Serveur (SSR / SSG) & Performance Web (Lighthouse)",
            "• Next.js (SSR vs SSG vs ISR) : Amélioration du temps de premier chargement (FCP) et du SEO par rapport à une Single Page Application classique.\n"
            "• Métriques Core Web Vitals : LCP (Largest Contentful Paint), FID / INP (Interactivity), CLS (Cumulative Layout Shift).\n"
            "• Optimisations : Lazy loading des composants, balise next/image, dynamic imports, mise en cache et minification des bundles."
        ),
        (
            "Protocoles de Communication : REST vs GraphQL",
            "• REST : Architecture basée sur des verbes HTTP (GET, POST, PUT, DELETE) et des endpoints fixes ; risque d'over-fetching ou d'under-fetching.\n"
            "• GraphQL : Un seul endpoint POST, le client demande exactement les champs souhaités via une requête structurée, idéal pour les UIs complexes."
        ),
        (
            "Sécurité Web : Bonnes Pratiques Frontend & Fullstack",
            "• JWT (JSON Web Token) : Stockage sécurisé (préférer les cookies HttpOnly/SameSite au localStorage contre les failles XSS).\n"
            "• XSS (Cross-Site Scripting) : Échapper les entrées utilisateurs, utiliser le sanitize et Content Security Policy (CSP).\n"
            "• CSRF (Cross-Site Request Forgery) : Utilisation de tokens anti-CSRF et de SameSite cookies.\n"
            "• Injections SQL : Utilisation systématique de requêtes préparées (ORMs / Parameterized queries)."
        ),
        (
            "Workflow Git Professionnel & Architecture de MediRDV",
            "• Gitflow : Branches `feature/*`, `develop`, `release/*`, `main` ; gestion des Pull Requests avec relecture obligatoire et commits conventionnels.\n"
            "• Architecture Modulaire / MVC de MediRDV : Séparation nette entre contrôleurs d'API, modèles de données, services métier et composants de présentation React."
        )
    ]

    for title, content in tech_topics:
        t_tbl = doc.add_table(rows=2, cols=1)
        t_tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
        c_th = t_tbl.cell(0, 0)
        c_tb = t_tbl.cell(1, 0)
        c_th.width = Inches(6.8)
        c_tb.width = Inches(6.8)
        
        set_cell_background(c_th, "1E293B")
        set_cell_background(c_tb, "FFFFFF")
        set_cell_margins(c_th, 50, 50, 110, 110)
        set_cell_margins(c_tb, 90, 90, 110, 110)
        
        pth = c_th.paragraphs[0]
        rth = pth.add_run(f"💻 {title}")
        rth.bold = True
        rth.font.color.rgb = RGBColor(255, 255, 255)
        rth.font.size = Pt(9.5)
        
        ptb = c_tb.paragraphs[0]
        ptb.paragraph_format.space_after = Pt(2)
        rtb = ptb.add_run(content)
        rtb.font.size = Pt(9)
        rtb.font.color.rgb = RGBColor(30, 41, 59)
        
        doc.add_paragraph().paragraph_format.space_after = Pt(2)

    # SAUVEGARDE & CONVERSION
    docx_path = os.path.abspath("TP4_Preparation_Entretien_ZetaBox.docx")
    pdf_path = os.path.abspath("TP4_Preparation_Entretien_ZetaBox.pdf")
    doc.save(docx_path)
    print("DOCX successfully generated at:", docx_path)

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
    build_tp4_document()
