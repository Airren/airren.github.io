---
title: 「Linux」 Make
---





```sh
.PHONY: clean   # .PHONY后面的target表示一个伪造的target，而不是真实存在的文件的target
clean:
	rm -rf *.out
```

