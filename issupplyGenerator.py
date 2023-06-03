import random
import csv

peaks62=["2W"]*4+["3W"]*4+["4W"]*4+["5W"]*4+["6W"]*4+["7W"]*5+["2S"]*4+["3S"]*4+["4S"]*4+["5S"]*4+["6S"]*4+["7S"]*5
peaks63=["2W"]*1+["3W"]*1+["4W"]*1+["5W"]*1+["6W"]*1+["7W"]*1+["2S"]*1+["3S"]*1+["4S"]*1+["5S"]*1+["6S"]*1+["7S"]*1
peaks64=["2W"]*1+["3W"]*1+["4W"]*1+["5W"]*1+["6W"]*1+["7W"]*1+["2S"]*1+["3S"]*1+["4S"]*1+["5S"]*1+["6S"]*1+["7S"]*1
items=['Potion','Firesand','Wooden Chair','Grilled Clam','Necklace','Coral Ring','Barbut','Macuahuitl','Sauerkraut','Baked Pumpkin','Tunic','Culinary Knife','Brush','Boiled Egg','Hora','Earrings','Butter','Brick Counter','Bronze Sheep','Growth Formula','Garnet Rapier','Spruce Round Shield','Shark Oil','Silver Ear Cuffs','Sweet Popoto','Parsnip Salad','Caramels','Ribbon','Rope','Cavalier\'s Hat','Horn','Salt Cod','Squid Ink','Essential Draught','Jam','Tomato Relish','Onion Soup','Pie','Corn Flakes','Pickled Radish','Iron Axe','Quartz Ring','Porcelain Vase','Vegetable Juice','Pumpkin Pudding','Sheepfluff Rug','Garden Scythe','Bed','Scale Fingers','Crook','Coral Sword','Coconut Juice','Honey','Seashine Opal','Dried Flowers','Powdered Paprika','Cawl Cennin','Isloaf','Popoto Salad','Dressing','Stove','Lantern','Natron','Bouillabaisse','Fossil Display','Bathtub','Spectacles','Cooling Glass','Runner Bean Saute','Beet Soup','Imam Bayildi','Pickled Zucchini']
shift={"2S":["2S","2S","2S","2S"],
"2W":["2W","2W","2W","2W"],
"3S":["U1","3S","3S","3S"],
"3W":["U1","67","3W","3W"],
"4S":["U1","45","4S","4S"],
"4W":["U1","45","4W","4W"],
"5S":["U1","45","5U","5S"],
"5W":["U1","45","5U","5W"],
"6S":["U1","67","67","6S"],
"6W":["U1","67","6W","6W"],
"7S":["U1","67","67","7S"],
"7W":["U1","67","67","7W"]}

with open("craft_peaks.csv",'w', newline='') as csvfile:
    week=csv.writer(csvfile,delimiter=',',quotechar='"',quoting=csv.QUOTE_MINIMAL)
    for i in range(41,1041):
        random.shuffle(peaks62)
        random.shuffle(peaks63)
        random.shuffle(peaks64)
        peaks=peaks62+peaks63+peaks64
        popularity=random.randint(0,99)
        for j in range(len(items)):
            if j==0:
                week.writerow([i]+[items[j]]+shift[peaks[j]]+[popularity])
            else:
                week.writerow([i]+[items[j]]+shift[peaks[j]]+[""])
