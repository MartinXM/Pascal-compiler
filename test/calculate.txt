program a; 
var
i,j,z:integer;
a,b,c:real;
begin
a:=0.2;
b:=-0.3;
c:=a+b-0.001;
writeln(c);
c:=a*b;
writeln(c);

if c<0.1 then
 writeln(1)
else
 writeln(0);

i:=2;
j:=-3;
z:=i*j;
writeln(z);
z:=i mod j;
writeln(z);
end.
