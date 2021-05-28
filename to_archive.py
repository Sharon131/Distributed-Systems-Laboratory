import sys
import zipfile
import os

if (len(sys.argv) < 2):
    print("Laboratory number not specified.")
    exit(0)

lab_number = str(sys.argv[1])
print("Zad number: ", sys.argv[1])

directory = "./Zad" + lab_number + "/src/main/java/"

newZipFile = zipfile.ZipFile("./Zad" + lab_number + "/Pastuła_Magdalena_" + lab_number + ".zip", mode='w')

newZipFile.write(directory, arcname="Pastuła_Magdalena_" + lab_number)

for filename in os.listdir(directory):
    if filename.endswith(".java"):
        newZipFile.write(directory + filename, arcname="Pastuła_Magdalena_" + lab_number + "/" + filename)

newZipFile.write("Oświadczenie.txt")

if lab_number == "2":
    newZipFile.write("./Zad2/zad2_schematic.jpg", arcname="schematic.jpg")

if lab_number == "4":
    newZipFile.write("./Zad4/akka_schematic.png", arcname="schematic.png")

if lab_number =="5":
    newZipFile.write("./Zad5/src/main/webapp/index.html", arcname="Pastuła_Magdalena_" + lab_number + "/" + "index.html")

newZipFile.close()