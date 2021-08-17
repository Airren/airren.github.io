help:
	@echo "d|debug          debug local"
	@echo "p|post           post to bytegopher"

d debug:
	cd public; rm -rf *; cd ..
	hugo
	hugo server
p post:
	hugo
	git add .; git commit -m "update"; git push;cd public;pwd;git add .;git commit -m "update";git push -f

