…or create a new repository on the command line

echo "# okta-angular-dpop-example" >> README.md
git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/wegamoto/okta-angular-dpop-example.git    https://github.com/wegamoto/javaMasterClass.git
git push -u origin main


…or push an existing repository from the command line
git remote add origin https://github.com/wegamoto/okta-angular-dpop-example.git
git branch -M main
git push -u origin main