import requests,bs4,json,bs4
from time import sleep

def dewhitespace(item : str):
    if len(item) == 0:
        return item
    chars=[' ','\n','\t','\r']
    try:
        while chars.__contains__(item[0]):
            item=item[1:]
        while chars.__contains__(item[-1]):
            item=item[:-1]
        while item.replace('  ',' ') != item:
            item=item.replace('  ',' ')
    except Exception as e:
        print(e.with_traceback())
    return item

url = 'https://meta.fabricmc.net/v1/versions'

verMC = ''
verYarn = ''
verAPI = ''
verLoader = ''

r = requests.get(url)
if r.status_code != 200:
    print('modmuss50 is down')
res = json.loads(r.text)

for x in res['game']:
    if x['stable']:
        verMC = x['version']
        break

for x in res['mappings']:
    if x['gameVersion'] == verMC:
        verYarn += x['separator']
        verYarn  = verYarn[1:]
        verYarn += str(x['build'] )
        verYarn += ":v2"
        break
verLoader = res['loader'][0]['version']
r = requests.get('https://maven.fabricmc.net/net/fabricmc/fabric-api/fabric-api/maven-metadata.xml')
soup = bs4.BeautifulSoup(r.text,features="html.parser")

version = soup.find_all('version')
shortMC = verMC.split('.')
shortMC = '%s.%s' % (shortMC[0],shortMC[1])
for x in range(len(version)):
    if version[-1*x].text.find(shortMC) > -1:
        verAPI = version[-1*x].text
        break

file = open('gradle.properties')
con = file.read()
file.close()
con = con.splitlines()
attrs = {}
names = ['minecraft_version','yarn_mappings','loader_version','fabric_version']
for x in range(len(con)):
    if con[x].__contains__('minecraft_version'):
        print('%s:%s -> %s' % ('minecraft_version',con[x].split('=')[1],verMC))
        con[x] = '%s=%s' % (con[x].split('=')[0],verMC)
    if con[x].__contains__('yarn_mappings'):
        print('%s:%s -> %s' % ('yarn_mappings',con[x].split('=')[1],verYarn))
        con[x] = '%s=%s' % (con[x].split('=')[0],verYarn)
    if con[x].__contains__('loader_version'):
        print('%s:%s -> %s' % ('loader_version',con[x].split('=')[1],verLoader))
        con[x] = '%s=%s' % (con[x].split('=')[0],verLoader)
    if con[x].__contains__('fabric_version'):
        print('%s:%s -> %s' % ('fabric_version',con[x].split('=')[1],verAPI))
        con[x] = '%s=%s' % (con[x].split('=')[0],verAPI)
file = open('gradle.properties','w+')
file.write('\n'.join(con))
file.close()