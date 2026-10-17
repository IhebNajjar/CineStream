import docx, copy, os, shutil
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH

source_path = r"C:\Users\ihebn\.gemini\antigravity\brain\7498dc77-956e-4abc-8050-1f0fef86e783\.user_uploaded\media_1792242375735_d9423eb0.docx"
target_path = r"c:\Users\ihebn\OneDrive\Documents\dsi31\proje de integration\Sprint 1 - CineStream.docx"
uploaded_target_path = r"C:\Users\ihebn\.gemini\antigravity\brain\7498dc77-956e-4abc-8050-1f0fef86e783\.user_uploaded\media_1792242375735_d9423eb0.docx"

doc = docx.Document(source_path)
body = doc._body._element

# The 4 screenshots to include in Section 8
captures = [
    {
        'img_path': r"c:\Users\ihebn\OneDrive\Documents\dsi31\proje de integration\screenshots\0_page_accueil.png",
        'caption': "Figure 8.1 – Page d'accueil : barre de navigation, bannière Hero Slider, chaînes partenaires et Top 5 des tendances"
    },
    {
        'img_path': r"c:\Users\ihebn\OneDrive\Documents\dsi31\proje de integration\screenshots\1_liste_paginee.png",
        'caption': "Figure 8.2 – Catalogue public : affichage en grille paginée (12 titres par page avec affichage des formats et tarifs)"
    },
    {
        'img_path': r"c:\Users\ihebn\OneDrive\Documents\dsi31\proje de integration\screenshots\2_recherche_et_filtres.png",
        'caption': "Figure 8.3 – Moteur de recherche et filtres combinés : requête dynamique (« Dune »), filtre de genre (« Science-Fiction ») et badges actifs"
    },
    {
        'img_path': r"c:\Users\ihebn\OneDrive\Documents\dsi31\proje de integration\screenshots\3_page_de_details.png",
        'caption': "Figure 8.4 – Fiche détaillée du film « The Dark Knight » : affiche, backdrop coordonné, synopsis, réalisation (Christopher Nolan), casting et options de location"
    }
]

# Table 3 is the original single-cell frame template
t3 = doc.tables[3]

# We need 4 tables total in section 8. Currently there are tables 3, 4, 5 (3 tables).
# Let's clone t3 to make a 4th table element.
new_tbl_el = copy.deepcopy(t3._element)

# Insert the new table element right after table 5 (at the end of section 8)
t5 = doc.tables[5]
t5_idx = list(body).index(t5._element)

# Add spacing paragraph then new table
spacing_p = docx.oxml.OxmlElement('w:p')
body.insert(t5_idx + 1, spacing_p)
body.insert(t5_idx + 2, new_tbl_el)

print(f"Total tables now: {len(doc.tables)}")

# Now populate tables 3, 4, 5, 6 with our 4 captures
section8_tables = [doc.tables[3], doc.tables[4], doc.tables[5], doc.tables[6]]

for idx, (tbl, item) in enumerate(zip(section8_tables, captures)):
    cell = tbl.cell(0, 0)
    
    # 1. Clear existing content in cell
    p_img = cell.paragraphs[0]
    p_img.text = ""
    p_img.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_img.paragraph_format.space_before = Pt(4)
    p_img.paragraph_format.space_after = Pt(6)
    
    # Add picture
    run_img = p_img.add_run()
    run_img.add_picture(item['img_path'], width=Inches(6.4))
    
    # 2. Add or configure caption paragraph
    if len(cell.paragraphs) > 1:
        p_cap = cell.paragraphs[1]
        p_cap.text = ""
    else:
        p_cap = cell.add_paragraph()
        
    p_cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p_cap.paragraph_format.space_before = Pt(4)
    p_cap.paragraph_format.space_after = Pt(4)
    
    run_cap = p_cap.add_run(item['caption'])
    run_cap.font.name = "Times New Roman"
    run_cap.font.size = Pt(9.5)
    run_cap.font.italic = True
    run_cap.font.color.rgb = RGBColor(85, 85, 85)
    
    print(f"Configured Table {idx + 1}: {item['caption'][:50]}...")

# Save updated document to project directory
doc.save(target_path)
print("Saved to:", target_path, f"({os.path.getsize(target_path) // 1024} KB)")

# Also sync with uploaded file
shutil.copy2(target_path, uploaded_target_path)
print("Synced with:", uploaded_target_path)
