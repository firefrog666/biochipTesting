Maximize
Subject to
w00001 + w00010 +  y000 = 1
w00001 + w00102 + w00111 + 2 y001 = 2
w00102 + w00203 + w00212 + 2 y002 = 2
w00203 + w00304 + w00313 + 2 y003 = 2
w00304 + w00414 + 2 y004 = 2
w00010 + w01011 + w01020 + 2 y010 = 2
w00111 + w01011 + w01112 + w01121 + 2 y011 = 2
w00212 + w01112 + w01213 + w01222 + 2 y012 = 2
w00313 + w01213 + w01314 + w01323 + 2 y013 = 2
w00414 + w01314 + w01424 + 2 y014 = 2
w01020 + w02021 + w02030 + 2 y020 = 2
w01121 + w02021 + w02122 + w02131 + 2 y021 = 2
w01222 + w02122 + w02223 + w02232 + 2 y022 = 2
w01323 + w02223 + w02324 + w02333 + 2 y023 = 2
w01424 + w02324 + w02434 + 2 y024 = 2
w02030 + w03031 + w03040 + 2 y030 = 2
w02131 + w03031 + w03132 + w03141 + 2 y031 = 2
w02232 + w03132 + w03233 + w03242 + 2 y032 = 2
w02333 + w03233 + w03334 + w03343 + 2 y033 = 2
w02434 + w03334 + w03444 + 2 y034 = 2
w03040 + w04041 + 2 y040 = 2
w03141 + w04041 + w04142 + 2 y041 = 2
w03242 + w04142 + w04243 + 2 y042 = 2
w03343 + w04243 + w04344 + 2 y043 = 2
w03444 + w04344 +  y044 = 1
  + f00001  + f00010 + flowStoG0 - gT000 = 0 
  - f00001  + f00102  + f00111 - gT001 = 0 
  - f00102  + f00203  + f00212 - gT002 = 0 
  - f00203  + f00304  + f00313 - gT003 = 0 
  - f00304  + f00414 - gT004 = 0 
  - f00010  + f01011  + f01020 - gT010 = 0 
  - f00111  - f01011  + f01112  + f01121 - gT011 = 0 
  - f00212  - f01112  + f01213  + f01222 - gT012 = 0 
  - f00313  - f01213  + f01314  + f01323 - gT013 = 0 
  - f00414  - f01314  + f01424 - gT014 = 0 
  - f01020  + f02021  + f02030 - gT020 = 0 
  - f01121  - f02021  + f02122  + f02131 - gT021 = 0 
  - f01222  - f02122  + f02223  + f02232 - gT022 = 0 
  - f01323  - f02223  + f02324  + f02333 - gT023 = 0 
  - f01424  - f02324  + f02434 - gT024 = 0 
  - f02030  + f03031  + f03040 - gT030 = 0 
  - f02131  - f03031  + f03132  + f03141 - gT031 = 0 
  - f02232  - f03132  + f03233  + f03242 - gT032 = 0 
  - f02333  - f03233  + f03334  + f03343 - gT033 = 0 
  - f02434  - f03334  + f03444 - gT034 = 0 
  - f03040  + f04041 - gT040 = 0 
  - f03141  - f04041  + f04142 - gT041 = 0 
  - f03242  - f04142  + f04243 - gT042 = 0 
  - f03343  - f04243  + f04344 - gT043 = 0 
  - f03444  - f04344 - gT044 = 0 
f00001 + 10000 w00001 >= 0
f00001 - 10000 w00001 <= 0
f03334 + 10000 w03334 >= 0
f03334 - 10000 w03334 <= 0
f02434 + 10000 w02434 >= 0
f02434 - 10000 w02434 <= 0
f04041 + 10000 w04041 >= 0
f04041 - 10000 w04041 <= 0
f01020 + 10000 w01020 >= 0
f01020 - 10000 w01020 <= 0
f01112 + 10000 w01112 >= 0
f01112 - 10000 w01112 <= 0
f02030 + 10000 w02030 >= 0
f02030 - 10000 w02030 <= 0
f00304 + 10000 w00304 >= 0
f00304 - 10000 w00304 <= 0
f02223 + 10000 w02223 >= 0
f02223 - 10000 w02223 <= 0
f03233 + 10000 w03233 >= 0
f03233 - 10000 w03233 <= 0
f00111 + 10000 w00111 >= 0
f00111 - 10000 w00111 <= 0
f00414 + 10000 w00414 >= 0
f00414 - 10000 w00414 <= 0
f01121 + 10000 w01121 >= 0
f01121 - 10000 w01121 <= 0
f00203 + 10000 w00203 >= 0
f00203 - 10000 w00203 <= 0
f02131 + 10000 w02131 >= 0
f02131 - 10000 w02131 <= 0
f03444 + 10000 w03444 >= 0
f03444 - 10000 w03444 <= 0
f02324 + 10000 w02324 >= 0
f02324 - 10000 w02324 <= 0
f01213 + 10000 w01213 >= 0
f01213 - 10000 w01213 <= 0
f00313 + 10000 w00313 >= 0
f00313 - 10000 w00313 <= 0
f00010 + 10000 w00010 >= 0
f00010 - 10000 w00010 <= 0
f02021 + 10000 w02021 >= 0
f02021 - 10000 w02021 <= 0
f02232 + 10000 w02232 >= 0
f02232 - 10000 w02232 <= 0
f04243 + 10000 w04243 >= 0
f04243 - 10000 w04243 <= 0
f04344 + 10000 w04344 >= 0
f04344 - 10000 w04344 <= 0
f03343 + 10000 w03343 >= 0
f03343 - 10000 w03343 <= 0
f03031 + 10000 w03031 >= 0
f03031 - 10000 w03031 <= 0
f01222 + 10000 w01222 >= 0
f01222 - 10000 w01222 <= 0
f00102 + 10000 w00102 >= 0
f00102 - 10000 w00102 <= 0
f01011 + 10000 w01011 >= 0
f01011 - 10000 w01011 <= 0
f04142 + 10000 w04142 >= 0
f04142 - 10000 w04142 <= 0
f03242 + 10000 w03242 >= 0
f03242 - 10000 w03242 <= 0
f02122 + 10000 w02122 >= 0
f02122 - 10000 w02122 <= 0
f03040 + 10000 w03040 >= 0
f03040 - 10000 w03040 <= 0
f02333 + 10000 w02333 >= 0
f02333 - 10000 w02333 <= 0
f01424 + 10000 w01424 >= 0
f01424 - 10000 w01424 <= 0
f00212 + 10000 w00212 >= 0
f00212 - 10000 w00212 <= 0
f03132 + 10000 w03132 >= 0
f03132 - 10000 w03132 <= 0
f03141 + 10000 w03141 >= 0
f03141 - 10000 w03141 <= 0
f01323 + 10000 w01323 >= 0
f01323 - 10000 w01323 <= 0
f01314 + 10000 w01314 >= 0
f01314 - 10000 w01314 <= 0
flowStoG0 >= 0
flowStoG0 + y000 + y001 + y002 + y003 + y004 + y010 + y011 + y012 + y013 + y014 + y020 + y021 + y022 + y023 + y024 + y030 + y031 + y032 + y033 + y034 + y040 + y041 + y042 + y043 + y044 = 25
w10001 + w10010 +  y100 = 1
w10001 + w10102 + w10111 + 2 y101 = 2
w10102 + w10203 + w10212 + 2 y102 = 2
w10203 + w10304 + w10313 + 2 y103 = 2
w10304 + w10414 + 2 y104 = 2
w10010 + w11011 + w11020 + 2 y110 = 2
w10111 + w11011 + w11112 + w11121 + 2 y111 = 2
w10212 + w11112 + w11213 + w11222 + 2 y112 = 2
w10313 + w11213 + w11314 + w11323 + 2 y113 = 2
w10414 + w11314 + w11424 + 2 y114 = 2
w11020 + w12021 + w12030 + 2 y120 = 2
w11121 + w12021 + w12122 + w12131 + 2 y121 = 2
w11222 + w12122 + w12223 + w12232 + 2 y122 = 2
w11323 + w12223 + w12324 + w12333 + 2 y123 = 2
w11424 + w12324 + w12434 + 2 y124 = 2
w12030 + w13031 + w13040 + 2 y130 = 2
w12131 + w13031 + w13132 + w13141 + 2 y131 = 2
w12232 + w13132 + w13233 + w13242 + 2 y132 = 2
w12333 + w13233 + w13334 + w13343 + 2 y133 = 2
w12434 + w13334 + w13444 + 2 y134 = 2
w13040 + w14041 + 2 y140 = 2
w13141 + w14041 + w14142 + 2 y141 = 2
w13242 + w14142 + w14243 + 2 y142 = 2
w13343 + w14243 + w14344 + 2 y143 = 2
w13444 + w14344 +  y144 = 1
  + f10001  + f10010 + flowStoG1 - gT100 = 0 
  - f10001  + f10102  + f10111 - gT101 = 0 
  - f10102  + f10203  + f10212 - gT102 = 0 
  - f10203  + f10304  + f10313 - gT103 = 0 
  - f10304  + f10414 - gT104 = 0 
  - f10010  + f11011  + f11020 - gT110 = 0 
  - f10111  - f11011  + f11112  + f11121 - gT111 = 0 
  - f10212  - f11112  + f11213  + f11222 - gT112 = 0 
  - f10313  - f11213  + f11314  + f11323 - gT113 = 0 
  - f10414  - f11314  + f11424 - gT114 = 0 
  - f11020  + f12021  + f12030 - gT120 = 0 
  - f11121  - f12021  + f12122  + f12131 - gT121 = 0 
  - f11222  - f12122  + f12223  + f12232 - gT122 = 0 
  - f11323  - f12223  + f12324  + f12333 - gT123 = 0 
  - f11424  - f12324  + f12434 - gT124 = 0 
  - f12030  + f13031  + f13040 - gT130 = 0 
  - f12131  - f13031  + f13132  + f13141 - gT131 = 0 
  - f12232  - f13132  + f13233  + f13242 - gT132 = 0 
  - f12333  - f13233  + f13334  + f13343 - gT133 = 0 
  - f12434  - f13334  + f13444 - gT134 = 0 
  - f13040  + f14041 - gT140 = 0 
  - f13141  - f14041  + f14142 - gT141 = 0 
  - f13242  - f14142  + f14243 - gT142 = 0 
  - f13343  - f14243  + f14344 - gT143 = 0 
  - f13444  - f14344 - gT144 = 0 
f10001 + 10000 w10001 >= 0
f10001 - 10000 w10001 <= 0
f13334 + 10000 w13334 >= 0
f13334 - 10000 w13334 <= 0
f12434 + 10000 w12434 >= 0
f12434 - 10000 w12434 <= 0
f14041 + 10000 w14041 >= 0
f14041 - 10000 w14041 <= 0
f11020 + 10000 w11020 >= 0
f11020 - 10000 w11020 <= 0
f11112 + 10000 w11112 >= 0
f11112 - 10000 w11112 <= 0
f12030 + 10000 w12030 >= 0
f12030 - 10000 w12030 <= 0
f10304 + 10000 w10304 >= 0
f10304 - 10000 w10304 <= 0
f12223 + 10000 w12223 >= 0
f12223 - 10000 w12223 <= 0
f13233 + 10000 w13233 >= 0
f13233 - 10000 w13233 <= 0
f10111 + 10000 w10111 >= 0
f10111 - 10000 w10111 <= 0
f10414 + 10000 w10414 >= 0
f10414 - 10000 w10414 <= 0
f11121 + 10000 w11121 >= 0
f11121 - 10000 w11121 <= 0
f10203 + 10000 w10203 >= 0
f10203 - 10000 w10203 <= 0
f12131 + 10000 w12131 >= 0
f12131 - 10000 w12131 <= 0
f13444 + 10000 w13444 >= 0
f13444 - 10000 w13444 <= 0
f12324 + 10000 w12324 >= 0
f12324 - 10000 w12324 <= 0
f11213 + 10000 w11213 >= 0
f11213 - 10000 w11213 <= 0
f10313 + 10000 w10313 >= 0
f10313 - 10000 w10313 <= 0
f10010 + 10000 w10010 >= 0
f10010 - 10000 w10010 <= 0
f12021 + 10000 w12021 >= 0
f12021 - 10000 w12021 <= 0
f12232 + 10000 w12232 >= 0
f12232 - 10000 w12232 <= 0
f14243 + 10000 w14243 >= 0
f14243 - 10000 w14243 <= 0
f14344 + 10000 w14344 >= 0
f14344 - 10000 w14344 <= 0
f13343 + 10000 w13343 >= 0
f13343 - 10000 w13343 <= 0
f13031 + 10000 w13031 >= 0
f13031 - 10000 w13031 <= 0
f11222 + 10000 w11222 >= 0
f11222 - 10000 w11222 <= 0
f10102 + 10000 w10102 >= 0
f10102 - 10000 w10102 <= 0
f11011 + 10000 w11011 >= 0
f11011 - 10000 w11011 <= 0
f14142 + 10000 w14142 >= 0
f14142 - 10000 w14142 <= 0
f13242 + 10000 w13242 >= 0
f13242 - 10000 w13242 <= 0
f12122 + 10000 w12122 >= 0
f12122 - 10000 w12122 <= 0
f13040 + 10000 w13040 >= 0
f13040 - 10000 w13040 <= 0
f12333 + 10000 w12333 >= 0
f12333 - 10000 w12333 <= 0
f11424 + 10000 w11424 >= 0
f11424 - 10000 w11424 <= 0
f10212 + 10000 w10212 >= 0
f10212 - 10000 w10212 <= 0
f13132 + 10000 w13132 >= 0
f13132 - 10000 w13132 <= 0
f13141 + 10000 w13141 >= 0
f13141 - 10000 w13141 <= 0
f11323 + 10000 w11323 >= 0
f11323 - 10000 w11323 <= 0
f11314 + 10000 w11314 >= 0
f11314 - 10000 w11314 <= 0
flowStoG1 >= 0
flowStoG1 + y100 + y101 + y102 + y103 + y104 + y110 + y111 + y112 + y113 + y114 + y120 + y121 + y122 + y123 + y124 + y130 + y131 + y132 + y133 + y134 + y140 + y141 + y142 + y143 + y144 = 25
w20001 + w20010 +  y200 = 1
w20001 + w20102 + w20111 + 2 y201 = 2
w20102 + w20203 + w20212 + 2 y202 = 2
w20203 + w20304 + w20313 + 2 y203 = 2
w20304 + w20414 + 2 y204 = 2
w20010 + w21011 + w21020 + 2 y210 = 2
w20111 + w21011 + w21112 + w21121 + 2 y211 = 2
w20212 + w21112 + w21213 + w21222 + 2 y212 = 2
w20313 + w21213 + w21314 + w21323 + 2 y213 = 2
w20414 + w21314 + w21424 + 2 y214 = 2
w21020 + w22021 + w22030 + 2 y220 = 2
w21121 + w22021 + w22122 + w22131 + 2 y221 = 2
w21222 + w22122 + w22223 + w22232 + 2 y222 = 2
w21323 + w22223 + w22324 + w22333 + 2 y223 = 2
w21424 + w22324 + w22434 + 2 y224 = 2
w22030 + w23031 + w23040 + 2 y230 = 2
w22131 + w23031 + w23132 + w23141 + 2 y231 = 2
w22232 + w23132 + w23233 + w23242 + 2 y232 = 2
w22333 + w23233 + w23334 + w23343 + 2 y233 = 2
w22434 + w23334 + w23444 + 2 y234 = 2
w23040 + w24041 + 2 y240 = 2
w23141 + w24041 + w24142 + 2 y241 = 2
w23242 + w24142 + w24243 + 2 y242 = 2
w23343 + w24243 + w24344 + 2 y243 = 2
w23444 + w24344 +  y244 = 1
  + f20001  + f20010 + flowStoG2 - gT200 = 0 
  - f20001  + f20102  + f20111 - gT201 = 0 
  - f20102  + f20203  + f20212 - gT202 = 0 
  - f20203  + f20304  + f20313 - gT203 = 0 
  - f20304  + f20414 - gT204 = 0 
  - f20010  + f21011  + f21020 - gT210 = 0 
  - f20111  - f21011  + f21112  + f21121 - gT211 = 0 
  - f20212  - f21112  + f21213  + f21222 - gT212 = 0 
  - f20313  - f21213  + f21314  + f21323 - gT213 = 0 
  - f20414  - f21314  + f21424 - gT214 = 0 
  - f21020  + f22021  + f22030 - gT220 = 0 
  - f21121  - f22021  + f22122  + f22131 - gT221 = 0 
  - f21222  - f22122  + f22223  + f22232 - gT222 = 0 
  - f21323  - f22223  + f22324  + f22333 - gT223 = 0 
  - f21424  - f22324  + f22434 - gT224 = 0 
  - f22030  + f23031  + f23040 - gT230 = 0 
  - f22131  - f23031  + f23132  + f23141 - gT231 = 0 
  - f22232  - f23132  + f23233  + f23242 - gT232 = 0 
  - f22333  - f23233  + f23334  + f23343 - gT233 = 0 
  - f22434  - f23334  + f23444 - gT234 = 0 
  - f23040  + f24041 - gT240 = 0 
  - f23141  - f24041  + f24142 - gT241 = 0 
  - f23242  - f24142  + f24243 - gT242 = 0 
  - f23343  - f24243  + f24344 - gT243 = 0 
  - f23444  - f24344 - gT244 = 0 
f20001 + 10000 w20001 >= 0
f20001 - 10000 w20001 <= 0
f23334 + 10000 w23334 >= 0
f23334 - 10000 w23334 <= 0
f22434 + 10000 w22434 >= 0
f22434 - 10000 w22434 <= 0
f24041 + 10000 w24041 >= 0
f24041 - 10000 w24041 <= 0
f21020 + 10000 w21020 >= 0
f21020 - 10000 w21020 <= 0
f21112 + 10000 w21112 >= 0
f21112 - 10000 w21112 <= 0
f22030 + 10000 w22030 >= 0
f22030 - 10000 w22030 <= 0
f20304 + 10000 w20304 >= 0
f20304 - 10000 w20304 <= 0
f22223 + 10000 w22223 >= 0
f22223 - 10000 w22223 <= 0
f23233 + 10000 w23233 >= 0
f23233 - 10000 w23233 <= 0
f20111 + 10000 w20111 >= 0
f20111 - 10000 w20111 <= 0
f20414 + 10000 w20414 >= 0
f20414 - 10000 w20414 <= 0
f21121 + 10000 w21121 >= 0
f21121 - 10000 w21121 <= 0
f20203 + 10000 w20203 >= 0
f20203 - 10000 w20203 <= 0
f22131 + 10000 w22131 >= 0
f22131 - 10000 w22131 <= 0
f23444 + 10000 w23444 >= 0
f23444 - 10000 w23444 <= 0
f22324 + 10000 w22324 >= 0
f22324 - 10000 w22324 <= 0
f21213 + 10000 w21213 >= 0
f21213 - 10000 w21213 <= 0
f20313 + 10000 w20313 >= 0
f20313 - 10000 w20313 <= 0
f20010 + 10000 w20010 >= 0
f20010 - 10000 w20010 <= 0
f22021 + 10000 w22021 >= 0
f22021 - 10000 w22021 <= 0
f22232 + 10000 w22232 >= 0
f22232 - 10000 w22232 <= 0
f24243 + 10000 w24243 >= 0
f24243 - 10000 w24243 <= 0
f24344 + 10000 w24344 >= 0
f24344 - 10000 w24344 <= 0
f23343 + 10000 w23343 >= 0
f23343 - 10000 w23343 <= 0
f23031 + 10000 w23031 >= 0
f23031 - 10000 w23031 <= 0
f21222 + 10000 w21222 >= 0
f21222 - 10000 w21222 <= 0
f20102 + 10000 w20102 >= 0
f20102 - 10000 w20102 <= 0
f21011 + 10000 w21011 >= 0
f21011 - 10000 w21011 <= 0
f24142 + 10000 w24142 >= 0
f24142 - 10000 w24142 <= 0
f23242 + 10000 w23242 >= 0
f23242 - 10000 w23242 <= 0
f22122 + 10000 w22122 >= 0
f22122 - 10000 w22122 <= 0
f23040 + 10000 w23040 >= 0
f23040 - 10000 w23040 <= 0
f22333 + 10000 w22333 >= 0
f22333 - 10000 w22333 <= 0
f21424 + 10000 w21424 >= 0
f21424 - 10000 w21424 <= 0
f20212 + 10000 w20212 >= 0
f20212 - 10000 w20212 <= 0
f23132 + 10000 w23132 >= 0
f23132 - 10000 w23132 <= 0
f23141 + 10000 w23141 >= 0
f23141 - 10000 w23141 <= 0
f21323 + 10000 w21323 >= 0
f21323 - 10000 w21323 <= 0
f21314 + 10000 w21314 >= 0
f21314 - 10000 w21314 <= 0
flowStoG2 >= 0
flowStoG2 + y200 + y201 + y202 + y203 + y204 + y210 + y211 + y212 + y213 + y214 + y220 + y221 + y222 + y223 + y224 + y230 + y231 + y232 + y233 + y234 + y240 + y241 + y242 + y243 + y244 = 25
w30001 + w30010 +  y300 = 1
w30001 + w30102 + w30111 + 2 y301 = 2
w30102 + w30203 + w30212 + 2 y302 = 2
w30203 + w30304 + w30313 + 2 y303 = 2
w30304 + w30414 + 2 y304 = 2
w30010 + w31011 + w31020 + 2 y310 = 2
w30111 + w31011 + w31112 + w31121 + 2 y311 = 2
w30212 + w31112 + w31213 + w31222 + 2 y312 = 2
w30313 + w31213 + w31314 + w31323 + 2 y313 = 2
w30414 + w31314 + w31424 + 2 y314 = 2
w31020 + w32021 + w32030 + 2 y320 = 2
w31121 + w32021 + w32122 + w32131 + 2 y321 = 2
w31222 + w32122 + w32223 + w32232 + 2 y322 = 2
w31323 + w32223 + w32324 + w32333 + 2 y323 = 2
w31424 + w32324 + w32434 + 2 y324 = 2
w32030 + w33031 + w33040 + 2 y330 = 2
w32131 + w33031 + w33132 + w33141 + 2 y331 = 2
w32232 + w33132 + w33233 + w33242 + 2 y332 = 2
w32333 + w33233 + w33334 + w33343 + 2 y333 = 2
w32434 + w33334 + w33444 + 2 y334 = 2
w33040 + w34041 + 2 y340 = 2
w33141 + w34041 + w34142 + 2 y341 = 2
w33242 + w34142 + w34243 + 2 y342 = 2
w33343 + w34243 + w34344 + 2 y343 = 2
w33444 + w34344 +  y344 = 1
  + f30001  + f30010 + flowStoG3 - gT300 = 0 
  - f30001  + f30102  + f30111 - gT301 = 0 
  - f30102  + f30203  + f30212 - gT302 = 0 
  - f30203  + f30304  + f30313 - gT303 = 0 
  - f30304  + f30414 - gT304 = 0 
  - f30010  + f31011  + f31020 - gT310 = 0 
  - f30111  - f31011  + f31112  + f31121 - gT311 = 0 
  - f30212  - f31112  + f31213  + f31222 - gT312 = 0 
  - f30313  - f31213  + f31314  + f31323 - gT313 = 0 
  - f30414  - f31314  + f31424 - gT314 = 0 
  - f31020  + f32021  + f32030 - gT320 = 0 
  - f31121  - f32021  + f32122  + f32131 - gT321 = 0 
  - f31222  - f32122  + f32223  + f32232 - gT322 = 0 
  - f31323  - f32223  + f32324  + f32333 - gT323 = 0 
  - f31424  - f32324  + f32434 - gT324 = 0 
  - f32030  + f33031  + f33040 - gT330 = 0 
  - f32131  - f33031  + f33132  + f33141 - gT331 = 0 
  - f32232  - f33132  + f33233  + f33242 - gT332 = 0 
  - f32333  - f33233  + f33334  + f33343 - gT333 = 0 
  - f32434  - f33334  + f33444 - gT334 = 0 
  - f33040  + f34041 - gT340 = 0 
  - f33141  - f34041  + f34142 - gT341 = 0 
  - f33242  - f34142  + f34243 - gT342 = 0 
  - f33343  - f34243  + f34344 - gT343 = 0 
  - f33444  - f34344 - gT344 = 0 
f30001 + 10000 w30001 >= 0
f30001 - 10000 w30001 <= 0
f33334 + 10000 w33334 >= 0
f33334 - 10000 w33334 <= 0
f32434 + 10000 w32434 >= 0
f32434 - 10000 w32434 <= 0
f34041 + 10000 w34041 >= 0
f34041 - 10000 w34041 <= 0
f31020 + 10000 w31020 >= 0
f31020 - 10000 w31020 <= 0
f31112 + 10000 w31112 >= 0
f31112 - 10000 w31112 <= 0
f32030 + 10000 w32030 >= 0
f32030 - 10000 w32030 <= 0
f30304 + 10000 w30304 >= 0
f30304 - 10000 w30304 <= 0
f32223 + 10000 w32223 >= 0
f32223 - 10000 w32223 <= 0
f33233 + 10000 w33233 >= 0
f33233 - 10000 w33233 <= 0
f30111 + 10000 w30111 >= 0
f30111 - 10000 w30111 <= 0
f30414 + 10000 w30414 >= 0
f30414 - 10000 w30414 <= 0
f31121 + 10000 w31121 >= 0
f31121 - 10000 w31121 <= 0
f30203 + 10000 w30203 >= 0
f30203 - 10000 w30203 <= 0
f32131 + 10000 w32131 >= 0
f32131 - 10000 w32131 <= 0
f33444 + 10000 w33444 >= 0
f33444 - 10000 w33444 <= 0
f32324 + 10000 w32324 >= 0
f32324 - 10000 w32324 <= 0
f31213 + 10000 w31213 >= 0
f31213 - 10000 w31213 <= 0
f30313 + 10000 w30313 >= 0
f30313 - 10000 w30313 <= 0
f30010 + 10000 w30010 >= 0
f30010 - 10000 w30010 <= 0
f32021 + 10000 w32021 >= 0
f32021 - 10000 w32021 <= 0
f32232 + 10000 w32232 >= 0
f32232 - 10000 w32232 <= 0
f34243 + 10000 w34243 >= 0
f34243 - 10000 w34243 <= 0
f34344 + 10000 w34344 >= 0
f34344 - 10000 w34344 <= 0
f33343 + 10000 w33343 >= 0
f33343 - 10000 w33343 <= 0
f33031 + 10000 w33031 >= 0
f33031 - 10000 w33031 <= 0
f31222 + 10000 w31222 >= 0
f31222 - 10000 w31222 <= 0
f30102 + 10000 w30102 >= 0
f30102 - 10000 w30102 <= 0
f31011 + 10000 w31011 >= 0
f31011 - 10000 w31011 <= 0
f34142 + 10000 w34142 >= 0
f34142 - 10000 w34142 <= 0
f33242 + 10000 w33242 >= 0
f33242 - 10000 w33242 <= 0
f32122 + 10000 w32122 >= 0
f32122 - 10000 w32122 <= 0
f33040 + 10000 w33040 >= 0
f33040 - 10000 w33040 <= 0
f32333 + 10000 w32333 >= 0
f32333 - 10000 w32333 <= 0
f31424 + 10000 w31424 >= 0
f31424 - 10000 w31424 <= 0
f30212 + 10000 w30212 >= 0
f30212 - 10000 w30212 <= 0
f33132 + 10000 w33132 >= 0
f33132 - 10000 w33132 <= 0
f33141 + 10000 w33141 >= 0
f33141 - 10000 w33141 <= 0
f31323 + 10000 w31323 >= 0
f31323 - 10000 w31323 <= 0
f31314 + 10000 w31314 >= 0
f31314 - 10000 w31314 <= 0
flowStoG3 >= 0
flowStoG3 + y300 + y301 + y302 + y303 + y304 + y310 + y311 + y312 + y313 + y314 + y320 + y321 + y322 + y323 + y324 + y330 + y331 + y332 + y333 + y334 + y340 + y341 + y342 + y343 + y344 = 25
w40001 + w40010 +  y400 = 1
w40001 + w40102 + w40111 + 2 y401 = 2
w40102 + w40203 + w40212 + 2 y402 = 2
w40203 + w40304 + w40313 + 2 y403 = 2
w40304 + w40414 + 2 y404 = 2
w40010 + w41011 + w41020 + 2 y410 = 2
w40111 + w41011 + w41112 + w41121 + 2 y411 = 2
w40212 + w41112 + w41213 + w41222 + 2 y412 = 2
w40313 + w41213 + w41314 + w41323 + 2 y413 = 2
w40414 + w41314 + w41424 + 2 y414 = 2
w41020 + w42021 + w42030 + 2 y420 = 2
w41121 + w42021 + w42122 + w42131 + 2 y421 = 2
w41222 + w42122 + w42223 + w42232 + 2 y422 = 2
w41323 + w42223 + w42324 + w42333 + 2 y423 = 2
w41424 + w42324 + w42434 + 2 y424 = 2
w42030 + w43031 + w43040 + 2 y430 = 2
w42131 + w43031 + w43132 + w43141 + 2 y431 = 2
w42232 + w43132 + w43233 + w43242 + 2 y432 = 2
w42333 + w43233 + w43334 + w43343 + 2 y433 = 2
w42434 + w43334 + w43444 + 2 y434 = 2
w43040 + w44041 + 2 y440 = 2
w43141 + w44041 + w44142 + 2 y441 = 2
w43242 + w44142 + w44243 + 2 y442 = 2
w43343 + w44243 + w44344 + 2 y443 = 2
w43444 + w44344 +  y444 = 1
  + f40001  + f40010 + flowStoG4 - gT400 = 0 
  - f40001  + f40102  + f40111 - gT401 = 0 
  - f40102  + f40203  + f40212 - gT402 = 0 
  - f40203  + f40304  + f40313 - gT403 = 0 
  - f40304  + f40414 - gT404 = 0 
  - f40010  + f41011  + f41020 - gT410 = 0 
  - f40111  - f41011  + f41112  + f41121 - gT411 = 0 
  - f40212  - f41112  + f41213  + f41222 - gT412 = 0 
  - f40313  - f41213  + f41314  + f41323 - gT413 = 0 
  - f40414  - f41314  + f41424 - gT414 = 0 
  - f41020  + f42021  + f42030 - gT420 = 0 
  - f41121  - f42021  + f42122  + f42131 - gT421 = 0 
  - f41222  - f42122  + f42223  + f42232 - gT422 = 0 
  - f41323  - f42223  + f42324  + f42333 - gT423 = 0 
  - f41424  - f42324  + f42434 - gT424 = 0 
  - f42030  + f43031  + f43040 - gT430 = 0 
  - f42131  - f43031  + f43132  + f43141 - gT431 = 0 
  - f42232  - f43132  + f43233  + f43242 - gT432 = 0 
  - f42333  - f43233  + f43334  + f43343 - gT433 = 0 
  - f42434  - f43334  + f43444 - gT434 = 0 
  - f43040  + f44041 - gT440 = 0 
  - f43141  - f44041  + f44142 - gT441 = 0 
  - f43242  - f44142  + f44243 - gT442 = 0 
  - f43343  - f44243  + f44344 - gT443 = 0 
  - f43444  - f44344 - gT444 = 0 
f40001 + 10000 w40001 >= 0
f40001 - 10000 w40001 <= 0
f43334 + 10000 w43334 >= 0
f43334 - 10000 w43334 <= 0
f42434 + 10000 w42434 >= 0
f42434 - 10000 w42434 <= 0
f44041 + 10000 w44041 >= 0
f44041 - 10000 w44041 <= 0
f41020 + 10000 w41020 >= 0
f41020 - 10000 w41020 <= 0
f41112 + 10000 w41112 >= 0
f41112 - 10000 w41112 <= 0
f42030 + 10000 w42030 >= 0
f42030 - 10000 w42030 <= 0
f40304 + 10000 w40304 >= 0
f40304 - 10000 w40304 <= 0
f42223 + 10000 w42223 >= 0
f42223 - 10000 w42223 <= 0
f43233 + 10000 w43233 >= 0
f43233 - 10000 w43233 <= 0
f40111 + 10000 w40111 >= 0
f40111 - 10000 w40111 <= 0
f40414 + 10000 w40414 >= 0
f40414 - 10000 w40414 <= 0
f41121 + 10000 w41121 >= 0
f41121 - 10000 w41121 <= 0
f40203 + 10000 w40203 >= 0
f40203 - 10000 w40203 <= 0
f42131 + 10000 w42131 >= 0
f42131 - 10000 w42131 <= 0
f43444 + 10000 w43444 >= 0
f43444 - 10000 w43444 <= 0
f42324 + 10000 w42324 >= 0
f42324 - 10000 w42324 <= 0
f41213 + 10000 w41213 >= 0
f41213 - 10000 w41213 <= 0
f40313 + 10000 w40313 >= 0
f40313 - 10000 w40313 <= 0
f40010 + 10000 w40010 >= 0
f40010 - 10000 w40010 <= 0
f42021 + 10000 w42021 >= 0
f42021 - 10000 w42021 <= 0
f42232 + 10000 w42232 >= 0
f42232 - 10000 w42232 <= 0
f44243 + 10000 w44243 >= 0
f44243 - 10000 w44243 <= 0
f44344 + 10000 w44344 >= 0
f44344 - 10000 w44344 <= 0
f43343 + 10000 w43343 >= 0
f43343 - 10000 w43343 <= 0
f43031 + 10000 w43031 >= 0
f43031 - 10000 w43031 <= 0
f41222 + 10000 w41222 >= 0
f41222 - 10000 w41222 <= 0
f40102 + 10000 w40102 >= 0
f40102 - 10000 w40102 <= 0
f41011 + 10000 w41011 >= 0
f41011 - 10000 w41011 <= 0
f44142 + 10000 w44142 >= 0
f44142 - 10000 w44142 <= 0
f43242 + 10000 w43242 >= 0
f43242 - 10000 w43242 <= 0
f42122 + 10000 w42122 >= 0
f42122 - 10000 w42122 <= 0
f43040 + 10000 w43040 >= 0
f43040 - 10000 w43040 <= 0
f42333 + 10000 w42333 >= 0
f42333 - 10000 w42333 <= 0
f41424 + 10000 w41424 >= 0
f41424 - 10000 w41424 <= 0
f40212 + 10000 w40212 >= 0
f40212 - 10000 w40212 <= 0
f43132 + 10000 w43132 >= 0
f43132 - 10000 w43132 <= 0
f43141 + 10000 w43141 >= 0
f43141 - 10000 w43141 <= 0
f41323 + 10000 w41323 >= 0
f41323 - 10000 w41323 <= 0
f41314 + 10000 w41314 >= 0
f41314 - 10000 w41314 <= 0
flowStoG4 >= 0
flowStoG4 + y400 + y401 + y402 + y403 + y404 + y410 + y411 + y412 + y413 + y414 + y420 + y421 + y422 + y423 + y424 + y430 + y431 + y432 + y433 + y434 + y440 + y441 + y442 + y443 + y444 = 25
pairTruep0index0 - w00001 >= 0
pairTruep0index0 - w00102 >= 0
pairTruep0index0 - w00001 - w00102 <= 0
pairTruep0index1 - w00102 >= 0
pairTruep0index1 - w00203 >= 0
pairTruep0index1 - w00102 - w00203 <= 0
pairTruep0index2 - w00203 >= 0
pairTruep0index2 - w00304 >= 0
pairTruep0index2 - w00203 - w00304 <= 0
pairTruep0index3 - w00414 >= 0
pairTruep0index3 - w01314 >= 0
pairTruep0index3 - w00414 - w01314 <= 0
pairTruep0index4 - w01213 >= 0
pairTruep0index4 - w01112 >= 0
pairTruep0index4 - w01213 - w01112 <= 0
pairTruep0index5 - w01011 >= 0
pairTruep0index5 - w01020 >= 0
pairTruep0index5 - w01011 - w01020 <= 0
pairTruep0index6 - w02021 >= 0
pairTruep0index6 - w02122 >= 0
pairTruep0index6 - w02021 - w02122 <= 0
pairTruep0index7 - w02223 >= 0
pairTruep0index7 - w02324 >= 0
pairTruep0index7 - w02223 - w02324 <= 0
pairTruep0index8 - w02434 >= 0
pairTruep0index8 - w03334 >= 0
pairTruep0index8 - w02434 - w03334 <= 0
pairTruep0index9 - w03233 >= 0
pairTruep0index9 - w03132 >= 0
pairTruep0index9 - w03233 - w03132 <= 0
pairTruep0index10 - w03031 >= 0
pairTruep0index10 - w03040 >= 0
pairTruep0index10 - w03031 - w03040 <= 0
pairTruep0index11 - w04041 >= 0
pairTruep0index11 - w04142 >= 0
pairTruep0index11 - w04041 - w04142 <= 0
pairTruep0index12 - w04243 >= 0
pairTruep0index12 - w04344 >= 0
pairTruep0index12 - w04243 - w04344 <= 0
pairTruep0index13 - w01020 >= 0
pairTruep0index13 - w02030 >= 0
pairTruep0index13 - w01020 - w02030 <= 0
pairTruep0index14 - w03040 >= 0
pairTruep0index14 - w04041 >= 0
pairTruep0index14 - w03040 - w04041 <= 0
pairTruep0index15 - w03141 >= 0
pairTruep0index15 - w02131 >= 0
pairTruep0index15 - w03141 - w02131 <= 0
pairTruep0index16 - w01121 >= 0
pairTruep0index16 - w00111 >= 0
pairTruep0index16 - w01121 - w00111 <= 0
pairTruep0index17 - w00102 >= 0
pairTruep0index17 - w00212 >= 0
pairTruep0index17 - w00102 - w00212 <= 0
pairTruep0index18 - w01222 >= 0
pairTruep0index18 - w02232 >= 0
pairTruep0index18 - w01222 - w02232 <= 0
pairTruep0index19 - w03242 >= 0
pairTruep0index19 - w04243 >= 0
pairTruep0index19 - w03242 - w04243 <= 0
pairTruep0index20 - w03343 >= 0
pairTruep0index20 - w02333 >= 0
pairTruep0index20 - w03343 - w02333 <= 0
pairTruep0index21 - w01323 >= 0
pairTruep0index21 - w00313 >= 0
pairTruep0index21 - w01323 - w00313 <= 0
pairTruep0index22 - w00304 >= 0
pairTruep0index22 - w00414 >= 0
pairTruep0index22 - w00304 - w00414 <= 0
pairTruep0index23 - w01424 >= 0
pairTruep0index23 - w02434 >= 0
pairTruep0index23 - w01424 - w02434 <= 0
pairTruep1index0 - w10001 >= 0
pairTruep1index0 - w10102 >= 0
pairTruep1index0 - w10001 - w10102 <= 0
pairTruep1index1 - w10102 >= 0
pairTruep1index1 - w10203 >= 0
pairTruep1index1 - w10102 - w10203 <= 0
pairTruep1index2 - w10203 >= 0
pairTruep1index2 - w10304 >= 0
pairTruep1index2 - w10203 - w10304 <= 0
pairTruep1index3 - w10414 >= 0
pairTruep1index3 - w11314 >= 0
pairTruep1index3 - w10414 - w11314 <= 0
pairTruep1index4 - w11213 >= 0
pairTruep1index4 - w11112 >= 0
pairTruep1index4 - w11213 - w11112 <= 0
pairTruep1index5 - w11011 >= 0
pairTruep1index5 - w11020 >= 0
pairTruep1index5 - w11011 - w11020 <= 0
pairTruep1index6 - w12021 >= 0
pairTruep1index6 - w12122 >= 0
pairTruep1index6 - w12021 - w12122 <= 0
pairTruep1index7 - w12223 >= 0
pairTruep1index7 - w12324 >= 0
pairTruep1index7 - w12223 - w12324 <= 0
pairTruep1index8 - w12434 >= 0
pairTruep1index8 - w13334 >= 0
pairTruep1index8 - w12434 - w13334 <= 0
pairTruep1index9 - w13233 >= 0
pairTruep1index9 - w13132 >= 0
pairTruep1index9 - w13233 - w13132 <= 0
pairTruep1index10 - w13031 >= 0
pairTruep1index10 - w13040 >= 0
pairTruep1index10 - w13031 - w13040 <= 0
pairTruep1index11 - w14041 >= 0
pairTruep1index11 - w14142 >= 0
pairTruep1index11 - w14041 - w14142 <= 0
pairTruep1index12 - w14243 >= 0
pairTruep1index12 - w14344 >= 0
pairTruep1index12 - w14243 - w14344 <= 0
pairTruep1index13 - w11020 >= 0
pairTruep1index13 - w12030 >= 0
pairTruep1index13 - w11020 - w12030 <= 0
pairTruep1index14 - w13040 >= 0
pairTruep1index14 - w14041 >= 0
pairTruep1index14 - w13040 - w14041 <= 0
pairTruep1index15 - w13141 >= 0
pairTruep1index15 - w12131 >= 0
pairTruep1index15 - w13141 - w12131 <= 0
pairTruep1index16 - w11121 >= 0
pairTruep1index16 - w10111 >= 0
pairTruep1index16 - w11121 - w10111 <= 0
pairTruep1index17 - w10102 >= 0
pairTruep1index17 - w10212 >= 0
pairTruep1index17 - w10102 - w10212 <= 0
pairTruep1index18 - w11222 >= 0
pairTruep1index18 - w12232 >= 0
pairTruep1index18 - w11222 - w12232 <= 0
pairTruep1index19 - w13242 >= 0
pairTruep1index19 - w14243 >= 0
pairTruep1index19 - w13242 - w14243 <= 0
pairTruep1index20 - w13343 >= 0
pairTruep1index20 - w12333 >= 0
pairTruep1index20 - w13343 - w12333 <= 0
pairTruep1index21 - w11323 >= 0
pairTruep1index21 - w10313 >= 0
pairTruep1index21 - w11323 - w10313 <= 0
pairTruep1index22 - w10304 >= 0
pairTruep1index22 - w10414 >= 0
pairTruep1index22 - w10304 - w10414 <= 0
pairTruep1index23 - w11424 >= 0
pairTruep1index23 - w12434 >= 0
pairTruep1index23 - w11424 - w12434 <= 0
pairTruep2index0 - w20001 >= 0
pairTruep2index0 - w20102 >= 0
pairTruep2index0 - w20001 - w20102 <= 0
pairTruep2index1 - w20102 >= 0
pairTruep2index1 - w20203 >= 0
pairTruep2index1 - w20102 - w20203 <= 0
pairTruep2index2 - w20203 >= 0
pairTruep2index2 - w20304 >= 0
pairTruep2index2 - w20203 - w20304 <= 0
pairTruep2index3 - w20414 >= 0
pairTruep2index3 - w21314 >= 0
pairTruep2index3 - w20414 - w21314 <= 0
pairTruep2index4 - w21213 >= 0
pairTruep2index4 - w21112 >= 0
pairTruep2index4 - w21213 - w21112 <= 0
pairTruep2index5 - w21011 >= 0
pairTruep2index5 - w21020 >= 0
pairTruep2index5 - w21011 - w21020 <= 0
pairTruep2index6 - w22021 >= 0
pairTruep2index6 - w22122 >= 0
pairTruep2index6 - w22021 - w22122 <= 0
pairTruep2index7 - w22223 >= 0
pairTruep2index7 - w22324 >= 0
pairTruep2index7 - w22223 - w22324 <= 0
pairTruep2index8 - w22434 >= 0
pairTruep2index8 - w23334 >= 0
pairTruep2index8 - w22434 - w23334 <= 0
pairTruep2index9 - w23233 >= 0
pairTruep2index9 - w23132 >= 0
pairTruep2index9 - w23233 - w23132 <= 0
pairTruep2index10 - w23031 >= 0
pairTruep2index10 - w23040 >= 0
pairTruep2index10 - w23031 - w23040 <= 0
pairTruep2index11 - w24041 >= 0
pairTruep2index11 - w24142 >= 0
pairTruep2index11 - w24041 - w24142 <= 0
pairTruep2index12 - w24243 >= 0
pairTruep2index12 - w24344 >= 0
pairTruep2index12 - w24243 - w24344 <= 0
pairTruep2index13 - w21020 >= 0
pairTruep2index13 - w22030 >= 0
pairTruep2index13 - w21020 - w22030 <= 0
pairTruep2index14 - w23040 >= 0
pairTruep2index14 - w24041 >= 0
pairTruep2index14 - w23040 - w24041 <= 0
pairTruep2index15 - w23141 >= 0
pairTruep2index15 - w22131 >= 0
pairTruep2index15 - w23141 - w22131 <= 0
pairTruep2index16 - w21121 >= 0
pairTruep2index16 - w20111 >= 0
pairTruep2index16 - w21121 - w20111 <= 0
pairTruep2index17 - w20102 >= 0
pairTruep2index17 - w20212 >= 0
pairTruep2index17 - w20102 - w20212 <= 0
pairTruep2index18 - w21222 >= 0
pairTruep2index18 - w22232 >= 0
pairTruep2index18 - w21222 - w22232 <= 0
pairTruep2index19 - w23242 >= 0
pairTruep2index19 - w24243 >= 0
pairTruep2index19 - w23242 - w24243 <= 0
pairTruep2index20 - w23343 >= 0
pairTruep2index20 - w22333 >= 0
pairTruep2index20 - w23343 - w22333 <= 0
pairTruep2index21 - w21323 >= 0
pairTruep2index21 - w20313 >= 0
pairTruep2index21 - w21323 - w20313 <= 0
pairTruep2index22 - w20304 >= 0
pairTruep2index22 - w20414 >= 0
pairTruep2index22 - w20304 - w20414 <= 0
pairTruep2index23 - w21424 >= 0
pairTruep2index23 - w22434 >= 0
pairTruep2index23 - w21424 - w22434 <= 0
pairTruep3index0 - w30001 >= 0
pairTruep3index0 - w30102 >= 0
pairTruep3index0 - w30001 - w30102 <= 0
pairTruep3index1 - w30102 >= 0
pairTruep3index1 - w30203 >= 0
pairTruep3index1 - w30102 - w30203 <= 0
pairTruep3index2 - w30203 >= 0
pairTruep3index2 - w30304 >= 0
pairTruep3index2 - w30203 - w30304 <= 0
pairTruep3index3 - w30414 >= 0
pairTruep3index3 - w31314 >= 0
pairTruep3index3 - w30414 - w31314 <= 0
pairTruep3index4 - w31213 >= 0
pairTruep3index4 - w31112 >= 0
pairTruep3index4 - w31213 - w31112 <= 0
pairTruep3index5 - w31011 >= 0
pairTruep3index5 - w31020 >= 0
pairTruep3index5 - w31011 - w31020 <= 0
pairTruep3index6 - w32021 >= 0
pairTruep3index6 - w32122 >= 0
pairTruep3index6 - w32021 - w32122 <= 0
pairTruep3index7 - w32223 >= 0
pairTruep3index7 - w32324 >= 0
pairTruep3index7 - w32223 - w32324 <= 0
pairTruep3index8 - w32434 >= 0
pairTruep3index8 - w33334 >= 0
pairTruep3index8 - w32434 - w33334 <= 0
pairTruep3index9 - w33233 >= 0
pairTruep3index9 - w33132 >= 0
pairTruep3index9 - w33233 - w33132 <= 0
pairTruep3index10 - w33031 >= 0
pairTruep3index10 - w33040 >= 0
pairTruep3index10 - w33031 - w33040 <= 0
pairTruep3index11 - w34041 >= 0
pairTruep3index11 - w34142 >= 0
pairTruep3index11 - w34041 - w34142 <= 0
pairTruep3index12 - w34243 >= 0
pairTruep3index12 - w34344 >= 0
pairTruep3index12 - w34243 - w34344 <= 0
pairTruep3index13 - w31020 >= 0
pairTruep3index13 - w32030 >= 0
pairTruep3index13 - w31020 - w32030 <= 0
pairTruep3index14 - w33040 >= 0
pairTruep3index14 - w34041 >= 0
pairTruep3index14 - w33040 - w34041 <= 0
pairTruep3index15 - w33141 >= 0
pairTruep3index15 - w32131 >= 0
pairTruep3index15 - w33141 - w32131 <= 0
pairTruep3index16 - w31121 >= 0
pairTruep3index16 - w30111 >= 0
pairTruep3index16 - w31121 - w30111 <= 0
pairTruep3index17 - w30102 >= 0
pairTruep3index17 - w30212 >= 0
pairTruep3index17 - w30102 - w30212 <= 0
pairTruep3index18 - w31222 >= 0
pairTruep3index18 - w32232 >= 0
pairTruep3index18 - w31222 - w32232 <= 0
pairTruep3index19 - w33242 >= 0
pairTruep3index19 - w34243 >= 0
pairTruep3index19 - w33242 - w34243 <= 0
pairTruep3index20 - w33343 >= 0
pairTruep3index20 - w32333 >= 0
pairTruep3index20 - w33343 - w32333 <= 0
pairTruep3index21 - w31323 >= 0
pairTruep3index21 - w30313 >= 0
pairTruep3index21 - w31323 - w30313 <= 0
pairTruep3index22 - w30304 >= 0
pairTruep3index22 - w30414 >= 0
pairTruep3index22 - w30304 - w30414 <= 0
pairTruep3index23 - w31424 >= 0
pairTruep3index23 - w32434 >= 0
pairTruep3index23 - w31424 - w32434 <= 0
pairTruep4index0 - w40001 >= 0
pairTruep4index0 - w40102 >= 0
pairTruep4index0 - w40001 - w40102 <= 0
pairTruep4index1 - w40102 >= 0
pairTruep4index1 - w40203 >= 0
pairTruep4index1 - w40102 - w40203 <= 0
pairTruep4index2 - w40203 >= 0
pairTruep4index2 - w40304 >= 0
pairTruep4index2 - w40203 - w40304 <= 0
pairTruep4index3 - w40414 >= 0
pairTruep4index3 - w41314 >= 0
pairTruep4index3 - w40414 - w41314 <= 0
pairTruep4index4 - w41213 >= 0
pairTruep4index4 - w41112 >= 0
pairTruep4index4 - w41213 - w41112 <= 0
pairTruep4index5 - w41011 >= 0
pairTruep4index5 - w41020 >= 0
pairTruep4index5 - w41011 - w41020 <= 0
pairTruep4index6 - w42021 >= 0
pairTruep4index6 - w42122 >= 0
pairTruep4index6 - w42021 - w42122 <= 0
pairTruep4index7 - w42223 >= 0
pairTruep4index7 - w42324 >= 0
pairTruep4index7 - w42223 - w42324 <= 0
pairTruep4index8 - w42434 >= 0
pairTruep4index8 - w43334 >= 0
pairTruep4index8 - w42434 - w43334 <= 0
pairTruep4index9 - w43233 >= 0
pairTruep4index9 - w43132 >= 0
pairTruep4index9 - w43233 - w43132 <= 0
pairTruep4index10 - w43031 >= 0
pairTruep4index10 - w43040 >= 0
pairTruep4index10 - w43031 - w43040 <= 0
pairTruep4index11 - w44041 >= 0
pairTruep4index11 - w44142 >= 0
pairTruep4index11 - w44041 - w44142 <= 0
pairTruep4index12 - w44243 >= 0
pairTruep4index12 - w44344 >= 0
pairTruep4index12 - w44243 - w44344 <= 0
pairTruep4index13 - w41020 >= 0
pairTruep4index13 - w42030 >= 0
pairTruep4index13 - w41020 - w42030 <= 0
pairTruep4index14 - w43040 >= 0
pairTruep4index14 - w44041 >= 0
pairTruep4index14 - w43040 - w44041 <= 0
pairTruep4index15 - w43141 >= 0
pairTruep4index15 - w42131 >= 0
pairTruep4index15 - w43141 - w42131 <= 0
pairTruep4index16 - w41121 >= 0
pairTruep4index16 - w40111 >= 0
pairTruep4index16 - w41121 - w40111 <= 0
pairTruep4index17 - w40102 >= 0
pairTruep4index17 - w40212 >= 0
pairTruep4index17 - w40102 - w40212 <= 0
pairTruep4index18 - w41222 >= 0
pairTruep4index18 - w42232 >= 0
pairTruep4index18 - w41222 - w42232 <= 0
pairTruep4index19 - w43242 >= 0
pairTruep4index19 - w44243 >= 0
pairTruep4index19 - w43242 - w44243 <= 0
pairTruep4index20 - w43343 >= 0
pairTruep4index20 - w42333 >= 0
pairTruep4index20 - w43343 - w42333 <= 0
pairTruep4index21 - w41323 >= 0
pairTruep4index21 - w40313 >= 0
pairTruep4index21 - w41323 - w40313 <= 0
pairTruep4index22 - w40304 >= 0
pairTruep4index22 - w40414 >= 0
pairTruep4index22 - w40304 - w40414 <= 0
pairTruep4index23 - w41424 >= 0
pairTruep4index23 - w42434 >= 0
pairTruep4index23 - w41424 - w42434 <= 0
 + pairTruep0index0 + pairTruep1index0 + pairTruep2index0 + pairTruep3index0 + pairTruep4index0 >= 1
 + pairTruep0index1 + pairTruep1index1 + pairTruep2index1 + pairTruep3index1 + pairTruep4index1 >= 1
 + pairTruep0index2 + pairTruep1index2 + pairTruep2index2 + pairTruep3index2 + pairTruep4index2 >= 1
 + pairTruep0index3 + pairTruep1index3 + pairTruep2index3 + pairTruep3index3 + pairTruep4index3 >= 1
 + pairTruep0index4 + pairTruep1index4 + pairTruep2index4 + pairTruep3index4 + pairTruep4index4 >= 1
 + pairTruep0index5 + pairTruep1index5 + pairTruep2index5 + pairTruep3index5 + pairTruep4index5 >= 1
 + pairTruep0index6 + pairTruep1index6 + pairTruep2index6 + pairTruep3index6 + pairTruep4index6 >= 1
 + pairTruep0index7 + pairTruep1index7 + pairTruep2index7 + pairTruep3index7 + pairTruep4index7 >= 1
 + pairTruep0index8 + pairTruep1index8 + pairTruep2index8 + pairTruep3index8 + pairTruep4index8 >= 1
 + pairTruep0index9 + pairTruep1index9 + pairTruep2index9 + pairTruep3index9 + pairTruep4index9 >= 1
 + pairTruep0index10 + pairTruep1index10 + pairTruep2index10 + pairTruep3index10 + pairTruep4index10 >= 1
 + pairTruep0index11 + pairTruep1index11 + pairTruep2index11 + pairTruep3index11 + pairTruep4index11 >= 1
 + pairTruep0index12 + pairTruep1index12 + pairTruep2index12 + pairTruep3index12 + pairTruep4index12 >= 1
 + pairTruep0index13 + pairTruep1index13 + pairTruep2index13 + pairTruep3index13 + pairTruep4index13 >= 1
 + pairTruep0index14 + pairTruep1index14 + pairTruep2index14 + pairTruep3index14 + pairTruep4index14 >= 1
 + pairTruep0index15 + pairTruep1index15 + pairTruep2index15 + pairTruep3index15 + pairTruep4index15 >= 1
 + pairTruep0index16 + pairTruep1index16 + pairTruep2index16 + pairTruep3index16 + pairTruep4index16 >= 1
 + pairTruep0index17 + pairTruep1index17 + pairTruep2index17 + pairTruep3index17 + pairTruep4index17 >= 1
 + pairTruep0index18 + pairTruep1index18 + pairTruep2index18 + pairTruep3index18 + pairTruep4index18 >= 1
 + pairTruep0index19 + pairTruep1index19 + pairTruep2index19 + pairTruep3index19 + pairTruep4index19 >= 1
 + pairTruep0index20 + pairTruep1index20 + pairTruep2index20 + pairTruep3index20 + pairTruep4index20 >= 1
 + pairTruep0index21 + pairTruep1index21 + pairTruep2index21 + pairTruep3index21 + pairTruep4index21 >= 1
 + pairTruep0index22 + pairTruep1index22 + pairTruep2index22 + pairTruep3index22 + pairTruep4index22 >= 1
 + pairTruep0index23 + pairTruep1index23 + pairTruep2index23 + pairTruep3index23 + pairTruep4index23 >= 1
Binaries
w00001
w03334
w02434
w04041
w01020
w01112
w02030
w00304
w02223
w03233
w00111
w00414
w01121
w00203
w02131
w03444
w02324
w01213
w00313
w00010
w02021
w02232
w04243
w04344
w03343
w03031
w01222
w00102
w01011
w04142
w03242
w02122
w03040
w02333
w01424
w00212
w03132
w03141
w01323
w01314
y000
y001
y002
y003
y004
y010
y011
y012
y013
y014
y020
y021
y022
y023
y024
y030
y031
y032
y033
y034
y040
y041
y042
y043
y044
w10001
w13334
w12434
w14041
w11020
w11112
w12030
w10304
w12223
w13233
w10111
w10414
w11121
w10203
w12131
w13444
w12324
w11213
w10313
w10010
w12021
w12232
w14243
w14344
w13343
w13031
w11222
w10102
w11011
w14142
w13242
w12122
w13040
w12333
w11424
w10212
w13132
w13141
w11323
w11314
y100
y101
y102
y103
y104
y110
y111
y112
y113
y114
y120
y121
y122
y123
y124
y130
y131
y132
y133
y134
y140
y141
y142
y143
y144
w20001
w23334
w22434
w24041
w21020
w21112
w22030
w20304
w22223
w23233
w20111
w20414
w21121
w20203
w22131
w23444
w22324
w21213
w20313
w20010
w22021
w22232
w24243
w24344
w23343
w23031
w21222
w20102
w21011
w24142
w23242
w22122
w23040
w22333
w21424
w20212
w23132
w23141
w21323
w21314
y200
y201
y202
y203
y204
y210
y211
y212
y213
y214
y220
y221
y222
y223
y224
y230
y231
y232
y233
y234
y240
y241
y242
y243
y244
w30001
w33334
w32434
w34041
w31020
w31112
w32030
w30304
w32223
w33233
w30111
w30414
w31121
w30203
w32131
w33444
w32324
w31213
w30313
w30010
w32021
w32232
w34243
w34344
w33343
w33031
w31222
w30102
w31011
w34142
w33242
w32122
w33040
w32333
w31424
w30212
w33132
w33141
w31323
w31314
y300
y301
y302
y303
y304
y310
y311
y312
y313
y314
y320
y321
y322
y323
y324
y330
y331
y332
y333
y334
y340
y341
y342
y343
y344
w40001
w43334
w42434
w44041
w41020
w41112
w42030
w40304
w42223
w43233
w40111
w40414
w41121
w40203
w42131
w43444
w42324
w41213
w40313
w40010
w42021
w42232
w44243
w44344
w43343
w43031
w41222
w40102
w41011
w44142
w43242
w42122
w43040
w42333
w41424
w40212
w43132
w43141
w41323
w41314
y400
y401
y402
y403
y404
y410
y411
y412
y413
y414
y420
y421
y422
y423
y424
y430
y431
y432
y433
y434
y440
y441
y442
y443
y444
pairTruep0index0
pairTruep0index1
pairTruep0index2
pairTruep0index3
pairTruep0index4
pairTruep0index5
pairTruep0index6
pairTruep0index7
pairTruep0index8
pairTruep0index9
pairTruep0index10
pairTruep0index11
pairTruep0index12
pairTruep0index13
pairTruep0index14
pairTruep0index15
pairTruep0index16
pairTruep0index17
pairTruep0index18
pairTruep0index19
pairTruep0index20
pairTruep0index21
pairTruep0index22
pairTruep0index23
pairTruep1index0
pairTruep1index1
pairTruep1index2
pairTruep1index3
pairTruep1index4
pairTruep1index5
pairTruep1index6
pairTruep1index7
pairTruep1index8
pairTruep1index9
pairTruep1index10
pairTruep1index11
pairTruep1index12
pairTruep1index13
pairTruep1index14
pairTruep1index15
pairTruep1index16
pairTruep1index17
pairTruep1index18
pairTruep1index19
pairTruep1index20
pairTruep1index21
pairTruep1index22
pairTruep1index23
pairTruep2index0
pairTruep2index1
pairTruep2index2
pairTruep2index3
pairTruep2index4
pairTruep2index5
pairTruep2index6
pairTruep2index7
pairTruep2index8
pairTruep2index9
pairTruep2index10
pairTruep2index11
pairTruep2index12
pairTruep2index13
pairTruep2index14
pairTruep2index15
pairTruep2index16
pairTruep2index17
pairTruep2index18
pairTruep2index19
pairTruep2index20
pairTruep2index21
pairTruep2index22
pairTruep2index23
pairTruep3index0
pairTruep3index1
pairTruep3index2
pairTruep3index3
pairTruep3index4
pairTruep3index5
pairTruep3index6
pairTruep3index7
pairTruep3index8
pairTruep3index9
pairTruep3index10
pairTruep3index11
pairTruep3index12
pairTruep3index13
pairTruep3index14
pairTruep3index15
pairTruep3index16
pairTruep3index17
pairTruep3index18
pairTruep3index19
pairTruep3index20
pairTruep3index21
pairTruep3index22
pairTruep3index23
pairTruep4index0
pairTruep4index1
pairTruep4index2
pairTruep4index3
pairTruep4index4
pairTruep4index5
pairTruep4index6
pairTruep4index7
pairTruep4index8
pairTruep4index9
pairTruep4index10
pairTruep4index11
pairTruep4index12
pairTruep4index13
pairTruep4index14
pairTruep4index15
pairTruep4index16
pairTruep4index17
pairTruep4index18
pairTruep4index19
pairTruep4index20
pairTruep4index21
pairTruep4index22
pairTruep4index23
Generals
f00001
f03334
f02434
f04041
f01020
f01112
f02030
f00304
f02223
f03233
f00111
f00414
f01121
f00203
f02131
f03444
f02324
f01213
f00313
f00010
f02021
f02232
f04243
f04344
f03343
f03031
f01222
f00102
f01011
f04142
f03242
f02122
f03040
f02333
f01424
f00212
f03132
f03141
f01323
f01314
gT000
gT001
gT002
gT003
gT004
gT010
gT011
gT012
gT013
gT014
gT020
gT021
gT022
gT023
gT024
gT030
gT031
gT032
gT033
gT034
gT040
gT041
gT042
gT043
gT044
flowStoG0
f10001
f13334
f12434
f14041
f11020
f11112
f12030
f10304
f12223
f13233
f10111
f10414
f11121
f10203
f12131
f13444
f12324
f11213
f10313
f10010
f12021
f12232
f14243
f14344
f13343
f13031
f11222
f10102
f11011
f14142
f13242
f12122
f13040
f12333
f11424
f10212
f13132
f13141
f11323
f11314
gT100
gT101
gT102
gT103
gT104
gT110
gT111
gT112
gT113
gT114
gT120
gT121
gT122
gT123
gT124
gT130
gT131
gT132
gT133
gT134
gT140
gT141
gT142
gT143
gT144
flowStoG1
f20001
f23334
f22434
f24041
f21020
f21112
f22030
f20304
f22223
f23233
f20111
f20414
f21121
f20203
f22131
f23444
f22324
f21213
f20313
f20010
f22021
f22232
f24243
f24344
f23343
f23031
f21222
f20102
f21011
f24142
f23242
f22122
f23040
f22333
f21424
f20212
f23132
f23141
f21323
f21314
gT200
gT201
gT202
gT203
gT204
gT210
gT211
gT212
gT213
gT214
gT220
gT221
gT222
gT223
gT224
gT230
gT231
gT232
gT233
gT234
gT240
gT241
gT242
gT243
gT244
flowStoG2
f30001
f33334
f32434
f34041
f31020
f31112
f32030
f30304
f32223
f33233
f30111
f30414
f31121
f30203
f32131
f33444
f32324
f31213
f30313
f30010
f32021
f32232
f34243
f34344
f33343
f33031
f31222
f30102
f31011
f34142
f33242
f32122
f33040
f32333
f31424
f30212
f33132
f33141
f31323
f31314
gT300
gT301
gT302
gT303
gT304
gT310
gT311
gT312
gT313
gT314
gT320
gT321
gT322
gT323
gT324
gT330
gT331
gT332
gT333
gT334
gT340
gT341
gT342
gT343
gT344
flowStoG3
f40001
f43334
f42434
f44041
f41020
f41112
f42030
f40304
f42223
f43233
f40111
f40414
f41121
f40203
f42131
f43444
f42324
f41213
f40313
f40010
f42021
f42232
f44243
f44344
f43343
f43031
f41222
f40102
f41011
f44142
f43242
f42122
f43040
f42333
f41424
f40212
f43132
f43141
f41323
f41314
gT400
gT401
gT402
gT403
gT404
gT410
gT411
gT412
gT413
gT414
gT420
gT421
gT422
gT423
gT424
gT430
gT431
gT432
gT433
gT434
gT440
gT441
gT442
gT443
gT444
flowStoG4
END
