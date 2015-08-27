# WAAT (Web Application Audit Tool) #


WAAT is a framework and audit tool which dynamically detects/scans vulnerability points in a web application.

[MITRE](http://www.mitre.org) found that all application security tool vendors claims put together cover only 45% of the known vulnerability types (695).
WAAT is an effort to include most of the present methodologies to create a complete dynamic analysis tool based on HTTP scan.

It can be used for most of the web applications and it doesn't care in which language the dynamic content of the web application is written. It is a eclipse current environment is..
JDK 1.6
Eclipse 3.4

WebGoat v5.2 which is a J2EE application developed for demonstration of possible vulnerabilities is very helpful in understanding the concepts and it is a nice way to learn about vulnerabilities for beginners.


### Administrators & Developers ###
_Udai Gupta_ (mailtoud@gmail.com)

_Hemant Purohit_ (hemantp.87@gmail.com)

_Tushar Dhaka_ (distini@gmail.com)
[The LNM Institute of Information Technology](http://lnmiit.ac.in)
### OBJECTIVE ###

The internet is growing at rapid phase, so is its popularity and usage. Web Applications are becoming more and more complex day by day. Nowadays more focus is on developing internet based application since it is better in terms of availability i.e. anywhere and anytime. The popularity has made web applications prone to attacks for unethical means which was not a serious issue a decade ago as there were a few web applications and only available to specific people. Our aim is to explore vulnerabilities and security issues related to Web Applications. We will analyze the present attacks and methodologies with their counter measures including implementation and testing. For being more specific in one direction, we will focus on Java/J2EE based Web Applications.


### APPROACH ###

In our understanding no dynamic analysis tool in open source implements all the possible way to achieve complete vulnerabilities analysis for and web application. There are good frameworks and tools for specific vulnerabilities but not complete solution hence there is a need for creating an analysis methodology which can cover the maximum possible domain of vulnerabilities. We will consider standard of OWASP ASVS 2008 Project as our assessment methodology, The main idea is to gather different frameworks and tools and create a step by step process to cover the maximum vulnerabilities domain and implement those features which we think are not there in any of the present tool using an existing framework.
In dynamic analysis approach we will focus on vulnerabilities one by one.
  1. SQL Injections
  1. XSS Injections
  1. Malicious code injection/upload
  1. CSRF – Cross Site Request Forgery

## CURRENT DIRECTIONS ##
Presently we are focusing on two major vulnerabilities SQL injections and XSS attacks.
The dynamic analysis tools we will use,
  1. [W3AF beta-7-r1812](http://w3af.sourceforge.net/)
  1. [WebScarab 20070504-1631](http://www.owasp.org/index.php/Category:OWASP_WebScarab_Project) / [WebScarab-NG](http://www.owasp.org/index.php/Category:OWASP_WebScarab_NG_Project)
  1. [Nikto v2](http://www.cirt.net/nikto2)
  1. [libwhisker 2.4](http://www.wiretrip.net/rfp/)
We will also look for vulnerability specific tools like for SQL Injections there is a tool
  1. [sqlmap](http://sqlmap.sourceforge.net/)
  1. [sqlninja](http://sqlninja.sourceforge.net/)

  * Look for vulnerability specific frameworks and tools available in public domain and compare their analysis results and choose the best and understand their merits and demerits, the complexity of their working.
  * Then make the whole process automated and user friendly by create the automation framework on the basis of results acquired in 1 step.



### REFERENCE ###

  1. Sean McAllister, Engin Kirda, and Christopher Kruegel, “Leveraging User Interactions for In-Depth Testing of Web Applications”, 11th International Symposium on Recent Advances in Intrusion Detection,Pages: 191-210, RAID 2008
  1. V. Benjamin Livshits and Monica S. Lam, “Finding Security Vulnerabilities in Java Applications with Static Analysis”. USENIX SEC, April 2005
  1. Yao-Wen Huang, Shih-Kun Huang, Tsung-Po Lin, Chung-Hung Tsai, “Web Application Security Assessment by Fault Injection and Behavior Monitoring”. WWW 2003, May 20-24, 2003, Budapest, Hungary. ACM 1-58113-680-3/03/0005.
  1. Christopher Kruegel, and Giovanni Vigna, “Cross-Site Scripting Prevention with Dynamic Data Tainting and Static Analysis”. Proceedings of the sixth annual IEEE/ACM international symposium on Code generation and optimization, Pages 74-83,CGO 2008, ISBN:978-1-59593-978-4
  1. Yao-Wen Huang, Fang Yu, Christian Hang, Chung-Hung Tsai, D. T. Lee, Sy-Yen Kuo, “Securing web application code by static analysis and runtime protection”, 13th international conference on World Wide Web, Pages: 40 - 52, WWW May, 2004, ISBN:1-58113-844-X
  1. OWASP TOP 10 Project Report, http://www.owasp.org/images/e/e8/OWASP_Top_10_2007.pdf
  1. Vulnerability Type Distribution in CVE data, http://cve.mitre.org/cve/data_sources.html
  1. IBM Java Security Research, http://www.research.ibm.com/javasec/
  1. Sun Research, http://research.sun.com/
  1. OWASP Java Project, http://www.owasp.org/index.php/Category:OWASP_Java_Project.
