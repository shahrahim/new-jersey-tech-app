import click
from rich.table import Table
from rich.console import Console
import requests

@click.group()
def main():
    pass

@main.command()
@click.option("--sid", default="")
@click.option("--cnumber", default="")
@click.option("--time", default="")
@click.option("--snumber", default="")
def register(sid, cnumber, time, snumber):
    url = "http://localhost:8080/api/v1/section/registration"

    if(sid == ""):
        exit()
    
    if(cnumber != "" and time != "" and snumber != ""):
        exit("Cant do that")

    if(cnumber != "" and time != ""):
        final_url = f"{url}/{sid}?courseNumber={cnumber}&time={time}"
        print(getPutResponse(final_url))

    if(snumber != ""):
        final_url = f"{url}/{sid}?sectionNumber={snumber}"
        print(getPutResponse(final_url))

@main.command()
def display():
    table = Table(show_header=True, header_style="bold green")
    console = Console()
    console.print("Section", "\n")

    response = getGetResponse(f"http://localhost:8080/api/v1/section/list")

    table.add_column("courseNumber")
    table.add_column("sectionNumber")
    table.add_column("rooms\n(time,weekday,roomNumber,buildingNumber)")
    table.add_column("facultySsn")
    table.add_column("courseName")
    table.add_column("students")
    
    for i in response:
        for k in i.keys():
            students_dict = {}
            number = i.get("number")
            courseNumber = i.get("courseNumber")

            response_c = getGetResponse(f"http://localhost:8080/api/v1/course/{courseNumber}")
            courseName = response_c.get("name")

            facultySsn = i.get("facultySsn")
            year = i.get("year")
            semester = i.get("semester")
            rooms = i.get("rooms")
            students = i.get("students")
            studs = []
            rIds = []
            for r in rooms:
                rIds.append(f"({r.get('time')},{r.get('weekday')},{r.get('roomNumber')},{r.get('buildingNumber')})") 

            for s in students:
                sid = s.get("sid")
                response_s = getGetResponse(f"http://localhost:8080/api/v1/student/{sid}")
                name = response_s.get("name")
                major = response_s.get("departmentCode")
                year = response_s.get("year")

                students_dict[name] = {
                    "sid": sid,
                    "major": major,
                    "year": year
                }


            # sort = sorted(students_dict.keys())
            sort = sorted(students_dict.keys(), key=lambda x: x.split(" ")[-1])
            for s in sort:
                name = s
                sid = students_dict.get(s).get("sid")
                major = students_dict.get(s).get("major")
                year = students_dict.get(s).get("year")
                studs.append(f"({name},{sid},{major},{year})")

        table.add_row(str(courseNumber),str(number), ' ,'.join(rIds), facultySsn, courseName, ','.join(studs))

    console = Console()
    console.print(table, "\n")




@main.command()
@click.option("--t", default="")
def tables(t):

    if(t == "all"):
        getBuildingList(t)
        getDepartmentList(t)
        getStaffList(t)
        getStudentList(t)
        getCourseList(t)
        getRoomList(t)
        getSectionList(t)

    if(t == "department"):
        getDepartmentList(t)

    if(t == "building"):
        getBuildingList(t)
        
    if(t == "staff"):
        getStaffList(t)

    if(t == "student"):
        getStudentList(t)

    if(t == "course"):
        getCourseList(t)

    if(t == "room"):
        getRoomList(t)

    if(t == "section"):
        getSectionList(t)


def getGetResponse(url):
    response = requests.get(url=url)

    if(response.status_code in [200, 201, 202]):
        return response.json()
    else:
        return []

def getPutResponse(url):
    response = requests.put(url=url)

    if(response.status_code in [200, 201, 202]):
        return response.json()
    else:
        print(response.text)
        return []

def getBuildingList(t):
    table = Table(show_header=True, header_style="bold green")
    console = Console()
    console.print("Building", "\n")

    response = getGetResponse(f"http://localhost:8080/api/v1/building/list")

    table.add_column("number")
    table.add_column("name")
    table.add_column("location")
    table.add_column("departments")
    table.add_column("rooms")

    for i in response:
        for k in i.keys():
            number = i.get("number")
            name = i.get("name")
            location = i.get("location")
                
            departments = i.get("departments")
            dCodes = []
            for d in departments:
                dCodes.append(d.get("code")) 

            rooms = i.get("rooms")
            rNums = []
            for r in rooms:
                rNums.append(str(r.get("number"))) 
                                
        table.add_row(str(number), name, location, str(','.join(dCodes)), str(rNums))

    console.print(table, "\n")

def getDepartmentList(t):
    table = Table(show_header=True, header_style="bold green")
    console = Console()
    console.print("Department", "\n")

    response = getGetResponse(f"http://localhost:8080/api/v1/department/list")

    table.add_column("code")
    table.add_column("name")
    table.add_column("chairSsn")
    table.add_column("buildingNumber")
    table.add_column("students")
    table.add_column("courses")
    table.add_column("faculties")

    for i in response:
        for k in i.keys():
            code = i.get("code")
            name = i.get("name")
            chairSsn = i.get("chairSsn")
            buildingNumber = i.get("buildingNumber")
                
            students = i.get("students")
            sids = []
            for s in students:
                sids.append(s.get("sid")) 

            courses = i.get("courses")
            cNums = []
            for c in courses:
                cNums.append(c.get("number")) 
                
            faculties = i.get("faculties")
            fSsns = []
            for f in faculties:
                fSsns.append(f.get("ssn")) 
                
        table.add_row(code, name, chairSsn, str(buildingNumber), str(','.join(sids)), str(cNums), str(','.join(fSsns)))

    console = Console()
    console.print(table, "\n")

def getStaffList(t):
    table = Table(show_header=True, header_style="bold green")
    console = Console()
    console.print("Staff", "\n")

    response = getGetResponse(f"http://localhost:8080/api/v1/staff/list")

    table.add_column("ssn")
    table.add_column("name")
    table.add_column("address")
    table.add_column("salary")
    table.add_column("type")
    table.add_column("rank")
    table.add_column("workHours")
    table.add_column("facultySections\n(section,course)")
    table.add_column("taSections\n(section,course)")
    table.add_column("departments")
    

    for i in response:
        for k in i.keys():
            ssn = i.get("ssn")
            name = i.get("name")
            address = i.get("address")
            salary = i.get("salary")
            ftype = i.get("type")
            rank = i.get("rank")
            workHours = i.get("workHours")
            facultySections = i.get("facultySections")
            taSections = i.get("taSections")
            departments = i.get("departments")
                
            fIds = []
            for f in facultySections:
                fIds.append(f"({f.get('number')},{f.get('courseNumber')})") 

            taIds = []
            for t in taSections:
                taIds.append(f"({t.get('number')},{t.get('courseNumber')})") 
                
            dCodes = []
            for d in departments:
                dCodes.append(d.get("code")) 
                
        table.add_row(ssn, name, address, str(salary), ftype, rank, str(workHours), ','.join(fIds), ','.join(taIds), ','.join(dCodes))

    console = Console()
    console.print(table, "\n")

def getStudentList(t):
    table = Table(show_header=True, header_style="bold green")
    console = Console()
    console.print("Student", "\n")

    response = getGetResponse(f"http://localhost:8080/api/v1/student/list")

    table.add_column("sid")
    table.add_column("ssn")
    table.add_column("department")
    table.add_column("name")
    table.add_column("address")
    table.add_column("highSchool")
    table.add_column("year")
    table.add_column("sections\n(section,course)")
    

    for i in response:
        for k in i.keys():
            sid = i.get("sid")
            ssn = i.get("ssn")
            department = i.get("departmentCode")
            name = i.get("name")
            address = i.get("address")
            highSchool = i.get("highSchool")
            year = i.get("year")
            sections = i.get("sections")
                            
            sIds = []
            for s in sections:
                sIds.append(f"({s.get('number')},{s.get('courseNumber')})") 

        table.add_row(sid, ssn, department, name, address, highSchool, str(year), ','.join(sIds))

    console = Console()
    console.print(table, "\n")

def getCourseList(t):
    table = Table(show_header=True, header_style="bold green")
    console = Console()
    console.print("Course", "\n")

    response = getGetResponse(f"http://localhost:8080/api/v1/course/list")

    table.add_column("number")
    table.add_column("name")
    table.add_column("department")
    table.add_column("credits")
    table.add_column("taHours")
    table.add_column("sections\n(section,course)")

    for i in response:
        for k in i.keys():
            number = i.get("number")
            name = i.get("name")
            department = i.get("departmentCode")
            creds = i.get("credits")
            taHours = i.get("taHours")
            sections = i.get("sections")
                            
            sIds = []
            for s in sections:
                sIds.append(f"({s.get('number')},{s.get('courseNumber')})") 

        table.add_row(str(number), name, department, str(creds), str(taHours), ','.join(sIds))

    console = Console()
    console.print(table, "\n")

def getRoomList(t):
    table = Table(show_header=True, header_style="bold green")
    console = Console()
    console.print("Room", "\n")

    response = getGetResponse(f"http://localhost:8080/api/v1/room/list")

    table.add_column("roomNumber")
    table.add_column("buildingNumber")
    table.add_column("capacity")
    table.add_column("audioVisual")
    table.add_column("sections\n(section,course,weekday,time)")

    for i in response:
        for k in i.keys():
            rNumber = i.get("roomNumber")
            bNumber = i.get("buildingNumber")
            capacity = i.get("capacity")
            audioVisual = i.get("audioVisual")
            sections = i.get("sections")
                            
            sIds = []
            for s in sections:
                sIds.append(f"({s.get('number')},{s.get('courseNumber')},{s.get('weekday')},{s.get('time')})") 

        table.add_row(str(rNumber), str(bNumber), str(capacity), audioVisual, ','.join(sIds))

    console = Console()
    console.print(table, "\n")

def getSectionList(t):
    table = Table(show_header=True, header_style="bold green")
    console = Console()
    console.print("Section", "\n")

    response = getGetResponse(f"http://localhost:8080/api/v1/section/list")

    table.add_column("sectionNumber")
    table.add_column("courseNumber")
    table.add_column("facultySsn")
    table.add_column("year")
    table.add_column("semester")
    table.add_column("maxEnroll")
    table.add_column("rooms\n(roomNumber,buildingNumber,weekday,time)")
    table.add_column("students")
    table.add_column("teachingAssistants")

    for i in response:
        for k in i.keys():
            number = i.get("number")
            courseNumber = i.get("courseNumber")
            facultySsn = i.get("facultySsn")
            year = i.get("year")
            semester = i.get("semester")
            maxEnroll = i.get("maxEnroll")
            rooms = i.get("rooms")
            students = i.get("students")
            teachingAssistants = i.get("teachingAssistants")
                            
            rIds = []
            for r in rooms:
                rIds.append(f"({r.get('roomNumber')},{r.get('buildingNumber')},{r.get('weekday')},{r.get('time')})") 

            sids = []
            for s in students:
                sids.append(s.get("sid")) 

            taSsns = []
            for t in teachingAssistants:
                taSsns.append(t.get("ssn")) 

        table.add_row(str(number), str(courseNumber), facultySsn, str(year), semester, str(maxEnroll), ','.join(rIds), ','.join(sids), ','.join(taSsns))

    console = Console()
    console.print(table, "\n")

if __name__ == "__main__":
    main()

