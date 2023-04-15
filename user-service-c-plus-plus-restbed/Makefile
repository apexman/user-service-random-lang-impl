CXX = g++
CXXFLAGS = -std=c++11 
RESTBEDFLAGS = -Ilibs/restbed/include  -Llibs/restbed/library -lrestbed
PQFLAGS = -Ilibs/postgre-client/include -Llibs/postgre-client/lib -lpq

all: server

server: source/server.cpp
	$(CXX) $(CXXFLAGS) $(RESTBEDFLAGS) $(PQFLAGS) source/*.cpp -o server

clean:
	rm -f server