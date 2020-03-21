import socket
from time import sleep

HOST = '192.168.14.34'
PORT = 5802 

while True:
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        try:
            print("trying to connect to server!")
            s.connect((HOST, PORT))
            while True:
                data = s.recv(1024).decode()
                if len(data) == 0:
                    print("connection close!")
                    break
                
                data = data.split(";")
                print(f"Target is valid: {data[0]}  X error: {data[1]}  Y error: {data[2]}")
                sleep(0.1)
        except:
            pass

