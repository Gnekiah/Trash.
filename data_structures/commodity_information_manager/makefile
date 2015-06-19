OBJECT = cims.o addInfo.o commodity.o deleteInfo.o help.o loadInfo.o logOff.o menu.o printInfo.o searchInfo.o 
HEAD = addInfo.h commodity.h deleteInfo.h help.h loadInfo.h logOff.h menu.h printInfo.h searchInfo.h
cims: $(OBJECT)
	g++ -o cims $(OBJECT)
cims.o: cims.cpp $(HEAD)
	g++ -c cims.cpp
addInfo.o: addInfo.cpp addInfo.h commodity.h
	g++ -c addInfo.cpp
commodity.o: commodity.cpp commodity.h
	g++ -c commodity.cpp
deleteInfo.o: deleteInfo.cpp deleteInfo.h
	g++ -c deleteInfo.cpp
help.o: help.cpp help.h
	g++ -c help.cpp
loadInfo.o: loadInfo.cpp loadInfo.h commodity.h
	g++ -c loadInfo.cpp
logOff.o: logOff.cpp logOff.h commodity.h
	g++ -c logOff.cpp
menu.o: menu.cpp menu.h
	g++ -c menu.cpp
printInfo.o: printInfo.cpp printInfo.h commodity.h
	g++ -c printInfo.cpp
searchInfo.o: searchInfo.cpp searchInfo.h commodity.h
	g++ -c searchInfo.cpp
.PHONY: clean
clean:
	rm $(OBJECT)
