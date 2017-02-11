import csv, os, operator, time
import csvdp


def __calc_grage_info(orgs):
    for i in orgs:
        i[1] = float(i[1])
        i[2] = float(i[2])
        i[4] = int(i[4])
    ori_list = []
    ext_list = []
    for i in orgs:
        if i[0] not in ori_list:
            ori_list.append(i[0])
            continue
        ext_list.append(i)
    tmp_orgs = []
    tmp_id = []
    for i in orgs:
        if i[0] not in tmp_id:
            tmp_id.append(i[0])
            tmp_orgs.append(i)
    orgs = tmp_orgs
    for i in ext_list:
        for j in orgs:
            if j[0] == i[0]:
                j[4] = 1
                if j[1] > 59:
                    j[1] = 60
                    break
                if i[1] > 59:
                    j[1] = 60
                else:
                    j[1] = 0
                break
    gpa = 0.0
    qua = len(orgs)
    fal = 0
    for i in orgs:
        if i[4] == 1 or i[1] < 59.9:
            fal += 1
        if i[1] > 90:
            gpa += 4.0
        elif i[1] < 59.9:
            pass
        else:
            gpa += (i[1] - 50) / 10
    gpa = gpa / qua
    return fal, qua, float(fal)/qua, gpa


def _calc_grade(Currents, sid):
    Historys = csvdp.load_csvlist('./historyexam/'+sid+'.csv', False)
    history_orgs = __calc_grage_info(Historys)
    current_orgs = __calc_grage_info(Currents)
    FailCnt = current_orgs[0]
    SubCnt = current_orgs[1]
    FailRate = current_orgs[2]
    HFailCnt = history_orgs[0]
    HGPA = history_orgs[3]
    return FailCnt, SubCnt, FailRate, HFailCnt, HGPA


def calc_grades(examname):
    examlist = csvdp.load_csvlist(examname)
    exams = list()
    sid = ""
    tmp = list()
    for i in examlist:
        if i[0] == sid:
            tmp.append(i[1:])
        elif sid == "":
            sid = i[0]
            tmp.append(i[1:])
        else:
            exams.append([sid, tmp])
            sid = i[0]
            tmp = list()
            tmp.append(i[1:])
    exams.append([sid, tmp])
    Grades = []
    for item in exams:
        grade = _calc_grade(item[1], item[0])
        anitem = [item[0]]
        anitem.extend(grade)
        Grades.append(anitem)
    return Grades ## [SID, FailCnt, SubCnt, FailRate, HFailCnt, HGPA]


def __check_time(time1, time2):
    if time1[0:10] == time2[0:10]:
        if int(time1[11:13]) == int(time2[11:13]):
            if int(time2[14:16]) - int(time1[14:16]) <= 30:
                return True
            return False
        if int(time2[11:13]) - int(time1[11:13]) == 1:
            if int(time2[14:16]) - int(time1[14:16]) <= -30:
                return True
            return False
        return False
    return False


def _round4(org1, org2):
    if org2 == 0 or org2 == 0.0:
        return 0
    return round(org1 / org2, 4)


def __calc_consum_info(orgs):
    NOT_MEAL = ['3','7','10','21','22','24','27','29','32','33','39','41','43','48','49']
    items = []
    item = orgs[-1]
    flag = False    # check if item had append to items, False - not appended
    for i in orgs[:-1][::-1]:
        if i[1] in NOT_MEAL:
            continue
        if __check_time(i[2], item[2]):
            i[3] = float(i[3]) + float(item[3])
            item = i
            flag = False
        else:
            items.append(item)
            item = i
            flag = True
    if not flag:
        items.append(item)
    sums = [0.0, 0.0, 0.0, 0.0] # [all, breakfast, lunch, dinner]
    days = [[], [], [], []]
    times = [len(items), 0, 0, 0]
    b_l = "10:00:00"
    l_d = "15:00:00"
    for i in items:
        sums[0] -= float(i[3])
        if i[2][:10]not in days[0]:
            days[0].append(i[2][:10])
        if i[2][11:] < b_l:
            sums[1] -= float(i[3])
            times[1] += 1
            if i[2][:10] not in days[1]:
                days[1].append(i[2][:10])
        elif i[2][11:] > b_l and i[2][11:] < l_d:
            sums[2] -= float(i[3])
            times[2] += 1
            if i[2][:10] not in days[2]:
                days[2].append(i[2][:10])
        else:
            sums[3] -= float(i[3])
            times[3] += 1
            if i[2][:10] not in days[3]:
                days[3].append(i[2][:10])
    days = [len(days[0]), len(days[1]), len(days[2]), len(days[3])]
    consumes = [sums[0], days[0], times[0]]
    consumes.extend([_round4(sums[0],days[0]), _round4(float(times[0]),days[0]), _round4(sums[0],times[0])])
    consumes.extend(sums[1:])
    consumes.extend(days[1:])
    consumes.extend(times[1:])
    consumes.extend([_round4(sums[1],days[1]), _round4(sums[2],days[2]), _round4(sums[3],days[3])])
    consumes.extend([_round4(float(times[1]),days[1]), _round4(float(times[2]),days[2]), _round4(float(times[3]),days[3])])
    consumes.extend([_round4(sums[1],times[1]), _round4(sums[2],times[2]), _round4(sums[3],times[3])])
    consumes.extend([_round4(sums[1],sums[0]), _round4(sums[2],sums[0]), _round4(sums[3],sums[0])])
    consumes.extend([_round4(float(days[1]),days[0]), _round4(float(days[2]),days[0]), _round4(float(days[3]),days[0])])
    consumes.extend([_round4(float(times[1]),times[0]), _round4(float(times[2]),times[0]), _round4(float(times[3]),times[0])])
    return consumes
    """
    CAllSum,CAllDays,CAllTimes,CPdaySum,CPdayTimes,CPtimeSum,CBAllSum,CLAllSum,\
    CDAllSum,CBAllDays,CLAllDays,CDAllDays,CBAllTimes,CLAllTimes,CDAllTimes,CBP\
    daySum,CLPdaySum,CDPdaySum,CBPdayTimes,CLPdayTimes,CDPdayTimes,CBPtimeSum,C\
    LPtimeSum,CDPtimeSum,CBSumRate,CLSumRate,CDSumRate,CBDaysRate,CLDaysRate,CD\
    DaysRate,CBTimesRate,CLTimesRate,CDTimesRate
    """


def _calc_consum(orgs):
    inner_list = []
    for i in orgs:
        inner_list.append(i)
    if len(inner_list) == 0:
        return orgs[0][0], 0, 0, 0, 0, 0
    consumes = __calc_consum_info(inner_list)
    return orgs[0][0], consumes[6], consumes[18], consumes[19], consumes[24], consumes[26]
    # [SID, CBAllSum, CBPdayTimes, CLPdayTimes, CBSumRate, CDSumRate]


def calc_consums(comsumename):
    conslist = csvdp.load_csvlist(comsumename)
    sid = ""
    conss = list()
    tmp = list()
    for i in conslist:
        if i[0] == sid:
            tmp.append(i)
        elif sid == "":
            sid = i[0]
            tmp.append(i)
        else:
            conss.append(tmp)
            sid = i[0]
            tmp = list()
            tmp.append(i)
    conss.append(tmp)
    Consums = []
    for cons in conss:
        consum = _calc_consum(cons)
        Consums.append(consum)
    return Consums


def __calc_workrest_info(orgs):
    meals = []
    huxi3_amount = 0.0
    days = []
    bre_days = []
    bre_amount = 0.0
    f_bre_amount = 0.0
    din_days = []
    din_amount = 0.0
    for i in orgs:
        if i[1] == '12':
            huxi3_amount += 1
        if i[1] not in meals:
            meals.append(i[1])
        if i[2][0:10] not in days:
            days.append(i[2][0:10])
        if i[2][11:16] <= "09:00":
            if i[2][0:10] not in bre_days:
                bre_days.append(i[2][0:10])
                bre_amount += 1
                if i[2][11:16] >= "07:00":
                    f_bre_amount += 1
        if i[2][11:16] >= "21:00":
            if i[2][0:10] not in din_days:
                din_days.append(i[2][0:10])
                din_amount += 1
    if len(orgs) == 0 or len(days) == 0:
        return 0,0,0,0,0,0,0
    return huxi3_amount/len(orgs), len(meals), f_bre_amount/len(days), bre_amount, bre_amount/len(days), din_amount, din_amount/len(days)
    # [W3MessRate, WMessCnt, WBLawRate, WEarlyCnt, WEarlyRate, WSupperCnt, WSupperRate]



def _calc_workrest(orgs):
    inner_list = []
    for i in orgs:
        inner_list.append(i)
    workrest = __calc_workrest_info(inner_list)
    return orgs[0][0], workrest[0], workrest[1], workrest[2], workrest[3], workrest[4]
    # [SID, W3MessRate, WMessCnt, WBLawRate, WEarlyCnt, WEarlyRate]


def calc_workrests(comsumename):
    conslist = csvdp.load_csvlist(comsumename)
    sid = ""
    conss = list()
    tmp = list()
    for i in conslist:
        if i[0] == sid:
            tmp.append(i)
        elif sid == "":
            sid = i[0]
            tmp.append(i)
        else:
            conss.append(tmp)
            sid = i[0]
            tmp = list()
            tmp.append(i)
    conss.append(tmp)
    Consums = []
    for cons in conss:
        consum = _calc_workrest(cons)
        Consums.append(consum)
    return Consums


def merge_all(examname, comsumename):
    Head = ['SID','FailCnt','SubCnt','FailRate','EthGroup','W3MessRate',
            'WMessCnt','WBLawRate','WEarlyCnt','WEarlyRate','CBAllSum',
            'CBPdayTimes','CLPdayTimes','CBSumRate','CDSumRate','HFailCnt','HGPA']
    Grades = calc_grades(examname)
    Consums = calc_consums(comsumename)
    Workrests = calc_workrests(comsumename)
    Infos = []
    info_list = csvdp.load_csvlist("information.csv")
    for i in info_list:
        Infos.append([i[0], i[6]])
    Merges = []
    for i in Grades: # [SID, FailCnt, SubCnt, FailRate, HFailCnt, HGPA]
        tmp = [i[0], i[1], i[2], i[3]]
        for j in Infos: # [SID, EthGroup]
            if i[0] == j[0]:
                tmp.append(j[1])
                Infos.remove(j)
                break
        for j in Workrests: # [SID, W3MessRate, WMessCnt, WBLawRate, WEarlyCnt, WEarlyRate]
            if i[0] == j[0]:
                tmp.extend(j[1:])
                Workrests.remove(j)
                break
        for j in Consums: # [SID, CBAllSum, CBPdayTimes, CLPdayTimes, CBSumRate, CDSumRate]
            if i[0] == j[0]:
                tmp.extend(j[1:])
                Consums.remove(j)
                break
        tmp.append(i[4])
        tmp.append(i[5])
        if len(tmp) != 17:
            continue
        Merges.append(tmp)
    csvdp.write_csvfile("term.csv", Merges, Head)
