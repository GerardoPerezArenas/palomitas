-- Esquema de pruebas
DEFINE esquema_pruebas = flbpru;
-- Esquema de producción
DEFINE esquema_organizacion = flb;
-- Esquema genérico
DEFINE esquema_generico = flbgen;

grant select ON &esquema_pruebas..A_UOR to &esquema_generico with grant option;
grant select ON &esquema_organizacion..A_UOR to &esquema_generico with grant option;
commit;