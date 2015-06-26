.386
.model flat,stdcall
option casemap:none
includelib msvcrt.lib
include masm32\include\msvcrt.inc
printf  proto C:dword,:dword
.data
lb_write_int db '%d',0
lb_writeln_int db '%d',0ah,0dh,0
lb_write_real db '%lf',0
lb_writeln_real db '%lf',0ah,0dh,0
lb_tmp db 0, 0, 0, 0, 0, 0, 0, 0
lb_read_int db '%d',0
lb_read_real db '%f',0
_REALNUM0 dd 1.340000
.code
main PROC
mov ecx, esp
sub esp, 8
mov edi, -8
mov esi, ecx
add esi, edi
mov eax, dword ptr [esi]
pusha
invoke crt_scanf, addr lb_read_real, addr lb_tmp
popa
mov eax, dword ptr lb_tmp
mov [esi], eax
mov edi, -8
mov esi, ecx
add esi, edi
mov eax, dword ptr [esi]
push eax
mov eax, dword ptr[_REALNUM0]; calculate real ExpConst 
push eax
fld dword ptr [esp+4]
fld dword ptr [esp]
pop eax
pop eax
fsub
sub esp,4
fstp dword ptr [esp]
pop eax
push eax
mov edi, -8
mov esi, ecx
add esi, edi
mov eax, dword ptr [esi]
pop eax
mov [esi-0], eax; assign
mov edi, -8
mov esi, ecx
add esi, edi
mov eax, dword ptr [esi]
pusha
push eax
fld dword ptr [esp]
sub esp, 4
fstp qword ptr [esp]
push offset lb_writeln_real
call printf
add esp, 8
pop eax
popa
add esp, 8
main ENDP
END main
