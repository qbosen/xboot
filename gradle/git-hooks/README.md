## Commit Message Template

```
<type>(<scope>): <subject>

<body>

<footer>
```

### HEADER
Header的部分只有一行,包括三个字段: type(必需), scope(可选), subject(必需)

#### type:  commit的类别

- feat: 新功能（feature）
- fix: 修补bug
- docs: 文档（documentation）
- style:  格式（不影响代码运行的变动,空格,格式化,等等）
- refactor: 重构（即不是新增功能，也不是修改bug的代码变动）
- perf: 性能 (提高代码性能的改变)
- test: 增加测试或者修改测试
- build: 影响构建系统或外部依赖项的更改(maven,gradle,npm 等等)
- ci: 对CI配置文件和脚本的更改
- chore: 对非 src 和 testModel 目录的修改
- revert: Revert a commit

#### scope: commit影响的范围

比如: route, component, utils, build...

或者具体的领域: userCO, order...

#### subject: commit的简短描述

不超过50个字符，末尾不加标点。

### Body

对commit的详细描述，可以为多行。

需要说明代码变动的原因和行为变化。

### Footer

1. 不兼容变动

   如果当前代码与上一个版本不兼容，则 Footer 部分以`BREAKING CHANGE`开头，后面是对变动的描述、以及变动理由和迁移方法。

2. 关闭Issue

   ```txt
   Closes #123, #245, #992
   ```

## Template

修改`~/gitconfig`，配置默认模版

```
# head: <type>(<scope>): <subject>
# - type: feat, fix, docs, style, refactor, pref, testModel, build, ci, chore, revert
# - scope: can be empty (eg. if the change is a global or difficult to assign to a single component)
# - subject: start with verb (such as 'change'), 50-character line
#
# body: 72-character wrapped. This should answer:
# * Why was this change necessary?
# * How does it address the problem?
# * Are there any side effects?
#
# footer: 
# - Include a link to the ticket, if any.
# - BREAKING CHANGE
# - Close issues
```



## Demo

[AngularJs Git Commit Message Conventions](https://docs.google.com/document/d/1QrDFcIiPjSLDn3EL15IJygNPiHORgU1_OOAqWjiDU5Y)

```
feat($compile): simplify isolate scope bindings

Changed the isolate scope binding options to:
  - @attr - attribute binding (including interpolation)
  - =model - by-directional model binding
  - &expr - expression execution binding

This change simplifies the terminology as well as
number of choices available to the developer. It
also supports local name aliasing from the parent.

Closes #392
BREAKING CHANGE: isolate scope bindings definition has changed and
the inject option for the directive controller injection was removed.

To migrate the code follow the example below:

Before:

scope: {
  myAttr: 'attribute',
  myBind: 'bind',
  myExpression: 'expression',
  myEval: 'evaluate',
  myAccessor: 'accessor'
}

After:

scope: {
  myAttr: '@',
  myBind: '@',
  myExpression: '&',
  // myEval - usually not useful, but in cases where the expression is assignable, you can use '='
  myAccessor: '=' // in directive's template change myAccessor() to myAccessor
}

```

