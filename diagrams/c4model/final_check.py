import re, html as html_mod, base64, zlib, urllib.parse

def decode_diagram(svg_path):
    with open(svg_path, encoding='utf-8') as f:
        svg = f.read()
    
    # Extract content attribute
    m = re.search(r'content="(.*?)"', svg)
    content_escaped = m.group(1)
    
    # HTML unescape to get mxfile XML
    mxfile = html_mod.unescape(content_escaped)
    
    # Extract diagram data
    m2 = re.search(r'<diagram[^>]*>(.*?)</diagram>', mxfile, re.DOTALL)
    raw_b64 = m2.group(1).strip()
    
    # Decode
    try:
        compressed = base64.b64decode(raw_b64)
        decompressed = zlib.decompress(compressed, -zlib.MAX_WBITS)
        xml = urllib.parse.unquote(decompressed.decode('utf-8'))
        
        # Check for mxGraphModel root
        if '<mxGraphModel' in xml and '<root>' in xml:
            # Count cells
            cell_count = xml.count('<mxCell ')
            edge_count = xml.count('edge="1"')
            print(f'  cells: {cell_count}, edges: {edge_count}')
            print(f'  first 300 chars of decoded XML:')
            print(f'  {xml[:300]}')
            return True
        else:
            print(f'  INVALID: no mxGraphModel/root')
            print(f'  first 500 chars: {xml[:500]}')
            return False
    except Exception as e:
        print(f'  ERROR decoding: {e}')
        return False

print('=== C1 (reference) ===')
decode_diagram('diagrams/c4model/ticketwave-c1-context.drawio.svg')

print()
print('=== C2 Containers ===')
decode_diagram('diagrams/c4model/ticketwave-c2-containers.drawio.svg')

print()
print('=== C2 Event Flow ===')
decode_diagram('diagrams/c4model/ticketwave-c2-eventflow.drawio.svg')
