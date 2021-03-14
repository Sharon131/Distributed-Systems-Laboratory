import sys
import zipfile
import os

if (len(sys.argv) < 2):
    print("Laboratory number not specified.")
    exit(0)

lab_number = str(sys.argv[1])
print("Lab number: ", sys.argv[1])

directory = "./Zad" + lab_number + "/src/main/java/"

newZipFile = zipfile.ZipFile("./Zad" + lab_number + "/Pastuła_Magdalena_" + lab_number + ".zip", mode='w')

newZipFile.write(directory, arcname="Pastuła_Magdalena_" + lab_number)

for filename in os.listdir(directory):
    if filename.endswith(".java"):
        newZipFile.write(directory + filename, arcname="Pastuła_Magdalena_" + lab_number + "/" + filename)

newZipFile.write("Oświadczenie.txt")

newZipFile.close()