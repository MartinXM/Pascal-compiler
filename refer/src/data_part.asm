.386
.model flat,stdcall
option casemap:none
include masm32\include\msvcrt.inc
includelib msvcrt.lib
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
