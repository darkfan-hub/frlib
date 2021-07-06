### 原有git项目引入子项目
```
gitee:
git submodule add https://gitee.com/gxb66/frlib.git

github:
git submodule add https://github.com/ff-xingyun/frlib.git
```

### 更新子项目
```
git submodule update --recursive --remote
```

### clone含有子项目的工程
```
git clone --recurse-submodules *****
```
