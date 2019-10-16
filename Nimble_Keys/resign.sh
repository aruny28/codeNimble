cp $1 $1.orig
zip -d $1 META-INF/\*.RSA META-INF/\*.SF META-INF/\*.MF
jarsigner -verbose -keystore /Users/tkma1d6/Nimble/nimble.keystore -storepass nimble $1 nimble 
mv $1 $1.1.apk
zipalign -v 4 $1.1.apk $1
rm $1.1.apk
