select * from tab;

show user;

/*
    sys 
        -오라클 Super 사용자이며 데이터 베이스에서 발생하는 모든 문제를 처리할
        수 있는 권한을 가진 사용자
    system
        -오라클 데이터베이스의 유지보수 관리할때 사용하는 사용자이며,
        sys사용자와 차이점은 데이터베이스 생성할 수 있는 권한이 없으며,
        데이터베이스 복구는 할 수가 없다.
    scott
        -사용자 계정 간단한 테이블에 접근할 수 있는 권한만 가질 수 있다.
*/
-- 테이블 구조 확인
desc emp;

select * from emp ;

/*
    select 
        데이터를 조회
    형식
        select 컬럼명, 컬럼,... from 테이블 명;
        - 컬럼명은 select 다음에 하나하나 명시할 수 있고,
        - 모든 컬럼을 명시할때는 (*)로 표시한다.
*/
desc dept;

select deptno, dname, loc from dept;

-- dept 테이블의 모든 내용을 출력
select * from dept;

-- emp 테이블의 모든 내용을 출력
select * from emp;

/*
    컬럼명에 별칭(별명) 지정
    - 컬럼명을 기술한 후 뒤에 as라는 키워드를 사용한 후 별칭을 기술하면 됨.
    - 별칭에는 공백, $, #, _이 세가지 특수문자를 사용하거나, 대소문자를 구분하고 싶으면
      " "를 사용한다.
      as라는 키워드를 생략하고  " "를 사용하여 별칭을 부여할 수 있다.
      별칭은 한글도 가능하다.
*/
select deptno "부서번호" from dept;

select job from emp;
-- 중복된 데이터를 한번만 출력 -> DISTINCT
select distinct job from emp;

-- 문] 사원들이 어떤부서에 소속되어 있는지 소속 부서번호를 출력하되, 중복되지않게 한번씩만 
--  출력하는 쿼리문을 작성하시오.

select distinct empno "부서번호", deptno from emp;


/* 
    특정 데이터를 추출하기 위한 연산자
    select문으로 쿼리문을 작성할때 where 절을 사용하여 데이터를 선택할때 
    조건을 부여할 수 있다.
    
    조건절에 해당하는 연산자의 종류
    비교연산자 
         = : 같다
         > : 크다
         < : 작다
         >= : 크거나 같다.
         <= : 작거나 같다.
         <>, !=, ^= : 같지않다.
*/
--문] 사원테이블에서 급여를 3000미만 받는 사원을 출력하시오.
select empno, ename, sal from emp where sal < 3000;
--문] emp 테이블에서 부서번호가 10번인 사원에 대한 모든 정보를 출력
select * from emp where deptno=10;
--문] emp테이블에서 급여가 2000미만이 되는 사원의 정보 중에서 사번, 이름, 급여 출력
select empno, ename, sal from emp where sal < 2000;

--문자 데이터 : 반드시 싱글쿼터(단일따옴표) 안에 표시되며, 대소문자를 구분함
--문] emp 테이블에서 이름이 SCOTT인 사원을 사번, 이름, 급여를 출력하는 쿼리문을 작성하시오
select empno, ename, sal from emp where ename = 'SCOTT';
--문] emp 테이블에서 이름이 MILLER인 사원을 사번, 이름, 급여를 출력하는 쿼리문을 작성하시오
select empno, ename, sal from emp where ename = 'MILLER';

select HIREDATE from emp;

--날짜데이터 조회 : 반드시 ''안에 표시 해야한다.
--기술형식 : 년/월/일
-- 문] 85년 이후에 입사한 사원을 이름과 입사일을 출력하시오
select ename, hiredate from emp where hiredate >'85/01/01';

/*
    논리 연산자 : and, or, not
        and : 두 가지 조건을 모두 만족해야만 검색하는 연산자
        or : 두 가지 조건 중 하나만 만족하더라도 검색하는 연산자
        not : 조건에 만족하지 못 하는 것만 검색하는 연산자
*/
--문] 부서번호가 10번이고, 직급이 manager인 사원을 이름, 부서번호, 직급을 출력하시오.
select ename, deptno,job from emp where deptno =10 and job='MANAGER';
--문] 급여가 1000에서 3000사이인 사원의 모든정보를 출력하시오.
select * from emp where sal >= 1000 and sal <= 3000;

/*
    BETWEEN AND 연산자 
    - 특정 범위 내에 속하는 데이터를 검색하고자 할때 사용하는 연산자
    형식 : 컬럼명 BETWEEN A AND B
    A와 B사이의 데이터를 검색
*/
--문] 급여가 1000에서 3000사이인 사원의 모든정보를 출력하시오.
select * from emp where sal between 1000 and 3000;

-- or : 두가지 중 한가지만 만족하더라도 검색하는 연산자
--문] 부서번호가 10번이거나 직급이 MANAGER인 사원의 이름 부서번호 직급을 출력하시오
select ename, deptno, job from emp where deptno = 10 or job ='MANAGER';
--문] 커미션이 300이거나 500이거나 1400인 사원의 이름, 부서번호, 커미션을 출력하시오.
select ename, deptno, comm from emp where comm = 300 or comm = 500 or comm = 1400;

/*
 in 연산자 
  - 동일한 컬럼이 여러개의 값 중에서 하나인지를 살펴보기 위해 간단하게 표현 할 수 있는 연산자
 형식 : 컬럼명 in(값1, 값2, 값3, ... )
*/
--문] 사원번호가 7844이거나, 7654이거나 7521인 사원으 정보를 출력하시오.
select empno, ename, deptno, job from emp where empno in(7844,7654,7521);

-- not : 조건에 만족하지 못 하는 것만 검색하는 연산자
-- 부서번호가 10번이 아닌 사원에 대한 정보를 출력하시오.(사원번호, 사원이름, 부서번호)
select empno, ename, deptno from emp where not deptno=10;
select empno, ename, deptno, job from emp where deptno not in(10);

select empno, ename, deptno, comm from emp where comm is null;

/*
    Like 연산자
    - 검색하고자 하는 값을 정확히 모를 경우 와일드 카드와 함께 사용하여
    원하는 내용을 검색하는 연산자
    
    형식 : 컬럼명 like 값(패턴:일정한 규칙)
    
    와일드 카드 종류
    % : 문자가 없거나, 하나 이상의 문자가 어떤 값이 오든 상관하지 않는다.
        like 's%' : 0 ~ n개 까지의 문자열을 대체할 수 있음.
    _ : 하나의 문자가 어떤값이 오든 상관하지않음
        like 's_' : s로 시작하는 두글자 문자열.
*/
-- 문] 사원의 이름이 K로 시작하는 사원의 번호와 이름을 출력하시오.
select empno, ename from emp where ename like 'K%';
-- 문] 사원의 이름이 K를 포함하는 사원의 번호와 이름을 출력하시오.
select empno, ename from emp where ename like '%K%';
-- 문] 사원의 이름이 K로 끝나는 사원의 번호와 이름을 출력하시오.
select empno, ename from emp where ename like '%K';

-- _는 한문자를 대신할 수 있다.
--문] 이름의 두번째 글자가 A인 사원의 번호와 이름을 출력하시오.
select empno, ename from emp where ename like '_A%';
--문] 이름에 A가 없는 사원의 번호와 이름을 출력하시오.
select empno, ename from emp where ename not like '%A%';


/*
    NULL을 위한 연산자
        오라클에서는 컬럼에 NULL값이 저장되는것을 허용함.
    
    NULL의 의미
        - 미확정
        - 알수없는 값
        - 0도 아니고, 빈공간도 아니고 어떤 값이 존재는 하지만
            어떤 값인지 알아 낼 수 없다는 의미
            
        NULL은 연산, 할당, 비교가 불가능함.
*/
select 100+null from dual;
-- 문] 커미션을 받지 않는 사원에 대하여 이름, 커미션, 직급을 출력하라.
select ename, comm, job from emp where comm=null; -- 비교, 연산이 불가하다.
select * from tab;
/*
    is null 과 is not null
        - 특정 컬럼 값인지를 비교할 경우에는 비교연산자를 사용하지 않고 is null 연산자를 사용하여
          null 값인지(is null) ,아닌지를(is not null)를 알아보려고 할때 사용한 연산자임
    is null : null값이면 만족
    is not null : null이 아니면 만족
*/

--문] 커미션을 받지 않는 사원에 대하여 이름, 커미션, 직급을 출력하라.
select ename, comm, job from emp where comm is null or comm <=0;-- 비교, 연산이 불가하다.
--문] 커미션을 받는 사원에 대하여 이름, 커미션, 직급을 출력하라.
select ename, comm, job from emp where comm is not null;-- 비교, 연산이 불가하다.

--문] 자신의 직속상관이 없는 사원의 이름과 직급, 직속상관의 사원번호를 출력하라.
select ename, job, mgr from emp where mgr is null;

/*
    dual table 
     - 한 행으로 결과를 출력하기 위한 테이블
     
    산술 연산의 결과를 얻으려고 가상컬럼 등의 값을 한번만 출력하고 싶을때 
    많이 사용하는 테이블이며, 하나의 컬러믕로 구성되어 있음.
    dummy라는 컬럼에는 한개의 문자만을 저장할 수 있는데 
    x라는 값을 가진 단 하나의 행만을 저장하고 있음.
*/
desc dual;
select * from dual;

/*
    정렬을 위한 ORDER BY 절
    order by 절은 행을 정렬하는데 사용하며, 쿼리문 맨 뒤에 기술 해야하며,
    정렬의 기준이 되는 컬럼 이름 또는 select 절에서 명시된 별칭을 사용할 수 있다.
    
                오름차순(ASC)           내림차순(DESC)
    숫자      작은 값부터 정렬           큰 값부터 정렬
    문자      사전 순으로 정렬           사전 반대 순으로 정렬
    날짜      빠른 날짜 순으로 정렬       느린 날짜 순으로 정렬
    NULL     가장 마지막에 나옴          가장먼저 출력됨

    영문자의 경우 소문자를 가장 큰 값으로, null값은 모든 값중 가장 작은 값으로 인식함.
*/
select * from emp;
--문] 사원 번호를 기준으로 오름차순 정렬하시오.
select empno, ename from emp order by empno;
--문] 사원 번호를 기준으로 내림차순 정렬하시오.
select empno, ename from emp order by empno desc;
--문] 사원의 번호, 이름, 급여를 급여가 가장 높은 순서대로 출력하시오.
select empno, ename, sal from emp order by sal desc;
--문] 사원의 번호, 이름, 급여를 입사일이 가장 최근 순서대로 출력하시오.
select empno, ename, hiredate from emp order by hiredate desc;

------------------함        수------------------
/*
    단일행 함수 : 행마다 적용되어 결과를 반환함.
        문자함수 : 문자열을 다른형태로 변환함.
            함수 종류 
            LOWER, UPPER, INITCAP, CONCAT, SUBSTR, LENGTH, INSTR, 
            LPAD, RPAD, TRIM, CONVERTE, CHR, ASCII, REPLACE
*/
select lower('AAAA') from dual; --소문자
select upper('AAAA') from dual; --대문자
select initcap('AAAA') from dual; --문자의 첫글자만 대문자
select concat('data','base') from dual; --문자를 합쳐서 출력
select SUBSTR('ABCDEFG',2,4) from dual; -- 2 = 위치 , 4 = 크기
select LENGTH('CANDIDE') from dual; -- 문자의 크기 출력
select trim('         aaaaaaaaaaaaa              ') from dual; --공백제거
select rtrim('         aaaaaaaaaaaaa              ') from dual; --오른쪽공백제거
select ltrim('         aaaaaaaaaaaaa              ') from dual; --왼쪽공백제거
/*
        숫자함수 : 숫자 값을 다른형태로 변환함.
        abs, cos, exp, floor, log, power, sign, sin, tan, round, trunc, mod
        floor : 소수점 이하는 버린다.
        round : 특정 자릿수에서 반올림함
*/
select floor(34.3552342325) from dual;

select round(834.12,1)from dual; -- 해당자릿수에서 반올림
select round(38.136472, 5) from dual;
select round(834.12,-1)from dual;

select mod(34,2) from dual;
select mod(34,5) from dual;

--문] 사원테이블에서 짝수인 사원들의 사번, 이름 직급을 출력하시오.
select empno, ename, job from emp where mod (empno, 2)=0; --2로나눠서 값이 0이면 출력
/*
        날짜함수 : 날짜 값을 다른 형태로 변환함.
        변환함수 : 문자, 숫자, 날짜 값을 다른타입으로 변환함.
        일반함수 : 기타함수 (NVL, DECODE, CASE)
        
        그룹함수 : 하나 이상의 행을 그룹으로 묶어 연산하여 하나의 결과로 반환함.
            sum
            avg
            count
            max
            min
            stddev
            variance
*/

