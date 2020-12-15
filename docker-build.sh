if [ $1 = NO-CACHE ]
then
   docker build --no-cache --tag atlas-aos:latest .
else
   docker build --tag atlas-aos:latest .
fi
