import urllib.request, json, os

req = urllib.request.Request('http://localhost:8080/api/media?page=0&size=32')
with urllib.request.urlopen(req) as res:
    data = json.loads(res.read().decode('utf-8'))

for m in data.get('content', []):
    m_id = m['id']
    title = m['title']
    poster_rel = m.get('posterUrl', '')
    poster_path = os.path.join('frontend/public', poster_rel.lstrip('/'))
    backdrop_path = f'frontend/public/assets/backdrops/{m_id}.jpg'
    
    poster_exists = os.path.exists(poster_path)
    bd_exists = os.path.exists(backdrop_path)
    
    print(f"ID {m_id:2d}: {title:<30} | Poster: {poster_rel} ({'OK' if poster_exists else 'MISSING'}) | Backdrop: {'OK' if bd_exists else 'MISSING'}")
