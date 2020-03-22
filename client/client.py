#!/usr/bin/env python3

import socket
import time

HOST = '192.168.14.34'  # The server's hostname or IP address
PORT = 5802        # The port used by the server

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PORT))
    
    while True:
        data = s.recv(1024)
        print("recev data:" + data.decode())
        time.sleep(1 / float(30)) 
        