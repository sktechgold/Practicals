import socket

def dns_lookup(query):
    try:
        socket.inet_aton(query)
        domain = socket.gethostbyaddr(query)[0]
        print(f"Domain name for IP '{query}' is: {domain}")
    except socket.herror:
        print(f"No domain found for IP: {query}")
    except socket.error:
        try:
            ip = socket.gethostbyname(query)
            print(f"IP address for domain '{query}' is: {ip}")
        except socket.gaierror:
            print(f"Could not resolve hostname: {query}")

if __name__ == "__main__":
    user_input = input("Enter a domain name or IP address: ").strip()
    dns_lookup(user_input)

