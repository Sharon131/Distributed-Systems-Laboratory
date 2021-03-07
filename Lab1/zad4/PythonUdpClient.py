import socket;

serverIP = "127.0.0.1"
serverPort = 9009
msg = "Ping Python Udp!"
to_send = '\0' + msg

print('PYTHON UDP CLIENT')
client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
client.sendto(bytes(to_send, 'cp1250'), (serverIP, serverPort))

buff = []  #
buff, address = client.recvfrom(20)

print((buff).decode('utf-8'))
