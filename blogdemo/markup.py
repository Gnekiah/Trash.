import os.path

GPATH = ""

## #title
## ##minor title
def __title(line):
    length = len(line)
    if length > 2 and line[1] == '#':
        return "<h2>" + line[2:] + "</h2>"
    if length > 1 and line[1] != '#':
        return "<h1>" + line[1:] + "</h1>"
    return "<h1></h1>"

## >tab
## >>minor tab
def __tab(line):
    length = len(line)
    if length > 2 and line[1] == '>':
        return "<blockquote><blockquote>" + line[2:] + "</blockquote></blockquote>"
    if length > 1 and line[1] != '>':
        return "<blockquote>" + line[1:] + "</blockquote>"
    return "<blockquote></blockquote>"

## ??
## ? int main() { return 0; }
## ?!
def __code(line):
    length = len(line)
    if length == 1:
        return "<li></li>"
    if line[1] == '?':
        return "<pre><ol>"
    if line[1] == '!':
        return "</ol></pre>"
    return "<li>" + line[1:] + "</li>"

## 0?
## 0. first
## 0. second
## 0!
def __order(line):
    length = len(line)
    if length == 1:
        return line + "<br/>"
    if line[1] == '?':
        return "<ol>"
    if line[1] == '!':
        return "</ol>"
    if length > 3:
        return "<li>" + line[3:] + "</li>"
    return line + "<br/>"

## *?
## *. first
## *. second
## *!
def __disorder(line):
    length = len(line)
    if length == 1:
        return line + "<br/>"
    if line[1] == '?':
        return "<ul>"
    if line[1] == '!':
        return "</ul>"
    if length > 3:
        return "<li>" + line[3:] + "</li>"
    return line + "<br/>"

## ====
def __bound(line):
    if len(line) > 3 and line[0:4] == '====':
        return "<HR/>"
    return line + "<br/>"

## ![img](1.jpg)
def __imgs(line):
    if len(line) == 1 or line[1] != '[':
        return line + "<br/>"
    lines = line.split("](")
    if len(lines) == 1 or lines[0] == None or lines[1] == None:
        return line + "<br/>"
    return "<br/><img src=\"" + GPATH + lines[1].strip(")") + "\"/><br/>"


dicts = {'#':__title, '>':__tab, '?':__code, '0':__order, '1':__order,
         '2':__order, '3':__order, '4':__order, '5':__order, '6':__order,
         '7':__order, '8':__order, '9':__order, '*':__disorder,
         '=':__bound, '!':__imgs }

## %%Strong%%
## ^^em^^
## $$s$$
## ``code``
## @@hyperlink^https://www.google.com@@
def __innerTrans(line):
    result = ""
    lines = line.split("%%")
    if len(lines) >= 3 and len(lines) % 2 == 1:
        cnt = 0
        while cnt < len(lines)-1:
            result += lines[cnt]
            cnt += 1
            result += "<strong>" + lines[cnt] + "</strong>"
            cnt += 1
        result += lines[cnt]
    else:
        result = line
    line = result
    result = ""
    lines = line.split("^^")
    if len(lines) >= 3 and len(lines) % 2 == 1:
        cnt = 0
        while cnt < len(lines)-1:
            result += lines[cnt]
            cnt += 1
            result += "<em>" + lines[cnt] + "</em>"
            cnt += 1
        result += lines[cnt]
    else:
        result = line
    line = result
    result = ""
    lines = line.split("$$")
    if len(lines) >= 3 and len(lines) % 2 == 1:
        cnt = 0
        while cnt < len(lines)-1:
            result += lines[cnt]
            cnt += 1
            result += "<s>" + lines[cnt] + "</s>"
            cnt += 1
        result += lines[cnt]
    else:
        result = line
    line = result
    result = ""
    lines = line.split("``")
    if len(lines) >= 3 and len(lines) % 2 == 1:
        cnt = 0
        while cnt < len(lines)-1:
            result += lines[cnt]
            cnt += 1
            result += "<code>" + lines[cnt] + "</code>"
            cnt += 1
        result += lines[cnt]
    else:
        result = line
    line = result
    result = ""
    lines = line.split("@@")
    if len(lines) >= 3 and len(lines) % 2 == 1:
        cnt = 0
        while cnt < len(lines)-1:
            result += lines[cnt]
            cnt += 1
            linesxx = lines[cnt].split("^")
            if len(linesxx) == 1:
                result += "<a" + lines[cnt] + "</a>"
            else:
                result += "<a target=\"_blank\" href=\"" + linesxx[1] + "\">" + linesxx[0] + "</a>"
            cnt += 1
        result += lines[cnt]
    else:
        result = line
    return result

## transform markup to html
## filename - .mu file path
## return html code
def markdo(text):
    marks = text.split('\n')
    html = ""
    for line in marks:
        length = len(line)
        if length == 0:
            html += "<br/>"
        else:
            if line[0] not in dicts:
                html += __innerTrans(line) + "<br/>"
            else:
                result = dicts.get(line[0])(line)
                html += __innerTrans(result)
    return html