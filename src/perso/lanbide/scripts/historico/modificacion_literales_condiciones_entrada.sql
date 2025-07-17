update A_TEX set TEX_DES='Si' where TEX_COD='etiq_accionFav' AND TEX_IDI=1;
update A_TEX set TEX_DES='No' where TEX_COD='etiq_accionDesFav' AND TEX_IDI=1;

update A_TEX set TEX_DES='Bai' where TEX_COD='etiq_accionFav' AND TEX_IDI=4;
update A_TEX set TEX_DES='Ez' where TEX_COD='etiq_accionDesFav' AND TEX_IDI=4;

commit;