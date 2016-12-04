rm -fr ~/.ivy2/cache/play1-base
cd api
play deps --sync
cd ../
cd jongo
play deps --sync
cd ../
play deps demo --sync
rm -fr demo/demo.i*
play idea demo
open demo/demo.ipr
