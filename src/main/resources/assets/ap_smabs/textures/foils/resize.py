from PIL import Image
import sys
import os

for x in os.listdir():
    if not x.endswith(".png"):
        continue
    i = Image.open(x)
    newi = Image.new('RGBA', (32, 32), (255, 255, 255, 0))
    newi.paste(i, (4, 1))
    os.rename(x, 'old/' + x)
    newi.save(x, quality=95)
