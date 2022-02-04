# Currently the combination of wget and gnutls seems to be broken on 32 bit ARM devices.
# HTTPS downloads fail with the following error message:
#
# root@qemuarm:~# wget -4 https://speed.hetzner.de/100MB.bin
# --2022-02-04 07:46:37--  https://speed.hetzner.de/100MB.bin
# SSL_INIT
# Resolving speed.hetzner.de... 88.198.248.254
# Connecting to speed.hetzner.de|88.198.248.254|:443... connected.
# The certificate has not yet been activated
#
# Therefore, we are currently building wget against openSSL.

PACKAGECONFIG:remove = "gnutls"
PACKAGECONFIG:append = " openssl"

