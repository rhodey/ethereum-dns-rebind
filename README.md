# ethereum-dns-rebind
Source code for [Walking Past Same-origin Policy, NAT, and Firewall for Ethereum Wallet Control](http://rhodey.org/blog/ethereum-wallet-exploit) article I wrote.

## Setup
```
# useradd -m taker
# su taker
$ cd /home/taker
$ git clone https://github.com/rhodey/eth-rebind app
$ cd app
$ ./bundle-js.sh
$ mvn package
```

## Configure
```
$ vim script/http-redirect.py
$ cp example-config.yml config.yml
```

## Test
```
$ python script/http-redirect.py
$ java -jar target/taker-x.x.x.jar server config.yml
```

## Install
```
# ln /home/taker/app/script/taker-python.service /lib/systemd/system/taker-python.service
# ln /home/taker/app/script/taker-java.service /lib/systemd/system/taker-java.service
# systemctl daemon-reload
# systemctl enable taker-python.service
# systemctl enable taker-java.service
```

## Run
```
# systemctl start taker-python.service
# systemctl start taker-java.service
```

## License

Copyright 2016 An Honest Effort LLC

Licensed under the GPLv3: http://www.gnu.org/licenses/gpl-3.0.html
