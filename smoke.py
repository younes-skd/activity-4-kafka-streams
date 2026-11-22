#!/usr/bin/env python3
import json, urllib.request, sys
def req(port,path,body=None,method=None):
    request=urllib.request.Request(f'http://localhost:{port}{path}',data=json.dumps(body).encode() if body is not None else None,method=method,headers={'Content-Type':'application/json'})
    with urllib.request.urlopen(request,timeout=180) as response:
        data=response.read();return json.loads(data) if data else None

import time
before=req(8095,'/api/stats').get('smoke',0)
req(8091,'/events',{'page':'smoke'})
for _ in range(40):
    if req(8095,'/api/stats').get('smoke',0)>before:break
    time.sleep(1)
else:raise AssertionError('No aggregate received')
with urllib.request.urlopen('http://localhost:8095/api/stream',timeout=5) as stream:
    assert 'event:counts' in stream.readline().decode()
print('PASS: producer → Kafka Streams → dashboard → SSE')
