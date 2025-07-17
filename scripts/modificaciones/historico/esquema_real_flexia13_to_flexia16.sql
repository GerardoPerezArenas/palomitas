
--Tarea #234108: Registro - Obligatoriedad documento interesado según asunto
--Milagros Noya (10/05/2016)
ALTER TABLE R_TIPOASUNTO ADD DOCINT_OBLIGATORIO NUMBER(1,0) DEFAULT 0;

COMMENT ON COLUMN R_TIPOASUNTO.DOCINT_OBLIGATORIO IS 'Indica si es obligatorio que el interesado tenga documento (tipo de documento distinto a "Sin documento"). 0:no; 1:si';
COMMIT;