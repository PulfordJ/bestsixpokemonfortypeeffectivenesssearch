file1 = open("xipokemonlist.txt", 'r')

lines = file1.readlines()

lineTuples = []
for i in range(0, len(lines), 2):
    typelist = []
    name = ""
    line = lines[i]+lines[i+1]
    words = line.split()[1:]
    numeric_found = False 
    for word in words:
        if word[0] == ':':
            typelist.append(word)
        elif not numeric_found and word.isnumeric():
            stat = word
            lineTuple = (stat, name, typelist)
            lineTuples.append(lineTuple)
            numeric_found = True
        else:
            name += word

print(lineTuples)
