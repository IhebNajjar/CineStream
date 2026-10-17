const puppeteer = require('puppeteer-core');
const path = require('path');
const fs = require('fs');

async function main() {
  const outputDir = path.join(__dirname, 'screenshots');
  if (!fs.existsSync(outputDir)) {
    fs.mkdirSync(outputDir, { recursive: true });
  }

  const browser = await puppeteer.launch({
    executablePath: 'C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe',
    headless: 'new',
    args: ['--no-sandbox', '--disable-setuid-sandbox', '--window-size=1920,1080']
  });

  const page = await browser.newPage();
  await page.setViewport({ width: 1920, height: 1080, deviceScaleFactor: 1 });

  // -------------------------------------------------------------
  // 1. Capture 1: Liste paginée
  // -------------------------------------------------------------
  console.log('1. Capturing Liste Paginée...');
  await page.goto('http://localhost:4200', { waitUntil: 'networkidle0' });
  await page.waitForSelector('.cards-grid app-media-card', { timeout: 10000 });
  await new Promise(r => setTimeout(r, 1200));

  // Scroll to show the catalogue cards and the pagination
  await page.evaluate(() => {
    const mainEl = document.querySelector('.catalogue-main');
    if (mainEl) {
      mainEl.scrollIntoView({ behavior: 'instant', block: 'start' });
    }
  });
  await new Promise(r => setTimeout(r, 600));
  await page.screenshot({ 
    path: path.join(outputDir, '1_liste_paginee.png'),
    fullPage: false 
  });
  console.log(' Saved 1_liste_paginee.png');

  // -------------------------------------------------------------
  // 2. Capture 2: Recherche et filtres
  // -------------------------------------------------------------
  console.log('2. Capturing Recherche et Filtres...');
  await page.goto('http://localhost:4200', { waitUntil: 'networkidle0' });
  await page.waitForSelector('.filter-panel', { timeout: 10000 });
  await new Promise(r => setTimeout(r, 1000));

  // Type search query & click Science-Fiction genre
  await page.type('.search-input', 'Dune');
  await page.evaluate(() => {
    // Click on Science-Fiction genre chip
    const chips = Array.from(document.querySelectorAll('.genre-chip'));
    const sciFi = chips.find(c => c.textContent.includes('Science-Fiction'));
    if (sciFi) sciFi.click();
  });
  await new Promise(r => setTimeout(r, 1000));

  // Position viewport right over the filter toolbar and filtered results
  await page.evaluate(() => {
    const toolbar = document.querySelector('.toolbar-section');
    if (toolbar) {
      toolbar.scrollIntoView({ behavior: 'instant', block: 'start' });
    }
  });
  await new Promise(r => setTimeout(r, 600));
  await page.screenshot({ 
    path: path.join(outputDir, '2_recherche_et_filtres.png'),
    fullPage: false 
  });
  console.log(' Saved 2_recherche_et_filtres.png');

  // -------------------------------------------------------------
  // 3. Capture 3: Page de détails
  // -------------------------------------------------------------
  console.log('3. Capturing Page de Détails...');
  await page.goto('http://localhost:4200/media/6', { waitUntil: 'networkidle0' });
  await page.waitForSelector('.main-poster', { timeout: 10000 });
  await page.waitForSelector('.cast-card', { timeout: 10000 });
  await new Promise(r => setTimeout(r, 1200));

  // Scroll to show poster, synopsis, trailer link, and cast
  await page.evaluate(() => {
    window.scrollTo({ top: 120, behavior: 'instant' });
  });
  await new Promise(r => setTimeout(r, 600));
  await page.screenshot({ 
    path: path.join(outputDir, '3_page_de_details.png'),
    fullPage: false 
  });
  console.log(' Saved 3_page_de_details.png');

  await browser.close();
  console.log('All 3 screenshots saved in:', outputDir);
}

main().catch(err => {
  console.error('Capture error:', err);
  process.exit(1);
});
