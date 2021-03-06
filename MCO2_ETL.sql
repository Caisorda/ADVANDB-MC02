/* CREATE FACT TABLE in db_hpq schema*/
USE db_hpq;
CREATE TABLE IF NOT EXISTS fact_table AS
SELECT hh.id AS hpq_hh_id, a.aquanitype AS hpq_aquani_id, aq.aquaequiptype AS hpq_aquaequip_id, a.aquani_vol
FROM hpq_hh hh, hpq_aquani a, hpq_aquaequip aq
WHERE hh.id = a.hpq_hh_id AND hh.id = aq.hpq_hh_id;

/* CREATE STAR SCHEMA */
CREATE SCHEMA IF NOT EXISTS star_schema;
USE star_schema;

/* COPY fact_table */
CREATE TABLE IF NOT EXISTS fact_table AS
SELECT *
FROM db_hpq.fact_table;

/* TRANSFORM DIMENTION TABLES */
CREATE TABLE IF NOT EXISTS hpq_hh AS
SELECT hh.id, hh.brgy, hh.mun
FROM db_hpq.hpq_hh hh;

CREATE TABLE IF NOT EXISTS hpq_aquani AS
SELECT distinct a.aquanitype as id, (CASE WHEN a.aquanitype=1 THEN 'TILAPIA'
                                             WHEN a.aquanitype=2 THEN 'MILKFISH'
                                             WHEN a.aquanitype=3 THEN 'CATFISH'
                                             WHEN a.aquanitype=4 THEN 'MUDFISH'
											 WHEN a.aquanitype=5 THEN 'CARP'
                                             WHEN a.aquanitype=6 THEN 'OTHER'
       					                END) aquani_type
FROM db_hpq.hpq_aquani a;

CREATE TABLE IF NOT EXISTS hpq_aquaequip AS
SELECT distinct a.aquaequiptype as id, (CASE WHEN a.aquaequiptype=1 THEN 'FISH NET'
                                             WHEN a.aquaequiptype=2 THEN 'ELECTRICITY'
                                             WHEN a.aquaequiptype=3 THEN 'BAGNETS'
                                             WHEN a.aquaequiptype=4 THEN 'GRILLNETS'
											 WHEN a.aquaequiptype=5 THEN 'TRAPS'
                                             WHEN a.aquaequiptype=6 THEN 'HOOKS AND LINE'
                                             WHEN a.aquaequiptype=7 THEN 'SIFT NET'
                                             WHEN a.aquaequiptype=8 THEN 'OTHER'
       					                END) equip_type
FROM db_hpq.hpq_aquaequip a;