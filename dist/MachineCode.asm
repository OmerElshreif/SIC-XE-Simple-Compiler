SUMPROG    START    0
    EXTREF    XREAD,XWRITE
     STL   RETADR
     +JSUB XREAD
     WORD  2
     WORD  N
     WORD  M
     LDA  #0
     STA   SUM
     LDA  #1
L0   STA  I
     COMP  N
     JGT  L1
     LDA  #1
L3   STA  J
     COMP  M
     JGT  L4
     LDA  I
     MULT  J
     ADD  SUM
     STA   SUM
     LDA  J
     ADD  #1
     J   L3
L4   LDA  I
     ADD  #1
     J   L0
L1   +JSUB XWRITE
     WORD  1
     WORD  SUM
N   RESW   1
SUM   RESW   1
I   RESW   1
M   RESW   1
L0   RESW   1
L1   RESW   1
L3   RESW   1
L4   RESW   1
