program a; 
type
 stu=record
 num:integer;
 price:real;
end;
var
 student:stu;

begin
student.num:=5;
student.price:=1.2;
writeln(student.num);
writeln(student.price);
end.
