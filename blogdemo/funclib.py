import os, random, time, hashlib, re


## authenticate user login
## login_name -
## login_passwd - using passwd and sn to calculate a long login passwd
## sn - a random serial number
## srv_passwd - passwd
def authenticating(login_name, login_passwd):
    # TODO check password and return True or False
    return False


## format article from HTML to MarkUp
## htmltext - submit from browser
## rela_path - blog dir or cland dir
## dir_name - dir to save images
def html_to_markup(htmltext, rela_path, dir_name):
    htmltext = htmltext.replace('&nbsp;', " ")
    htmltext = htmltext.replace('&lt;', "<")
    htmltext = htmltext.replace('&gt;', ">")
    htmltext = htmltext.replace('&amp;', "&")
    htmltext = htmltext.replace('&quot;', "\"")
    htmltext = htmltext.replace('&apos;', "\'")
    htmltext = htmltext.replace('<div>', "")
    htmltext = htmltext.replace('<br></div>', "\n")
    htmltext = htmltext.replace('</div>', "\n")
    if rela_path[-1] != "/":
        rela_path += "/"
    htmltext = htmltext.replace('src="/static/cache', 'src="/'+rela_path+dir_name)
    return htmltext


## log
def write_log(log, path):
    with open(path, "a") as f:
        f.write(log)
