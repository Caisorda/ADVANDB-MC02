/*#1: basic; Total volume of fish caught*/
select total_aquani_volume as volume
from cube
where hpq_hh_id is null and hpq_aquani_id is null and hpq_aquaequip_id is null;

/*#1CONVERTED*/
SELECT SUM(aquani_vol)
FROM hpq_aquani a
INNER JOIN hpq_hh h
ON (h.`main.id` = a.`main.id`)
WHERE aquanitype_o is not null;


/*#2: How much fish was caught in '24'*/
select total_aquani_volume as volume, hpq_hh.mun as municipality
from cube
inner join hpq_hh on (cube.hpq_hh_id = hpq_hh.id) 
where hpq_aquani_id is null and hpq_aquaequip_id is null and hpq_hh.mun = 24
group by hpq_hh.mun;

/*#2 CONVERTED*/
SELECT aquani_vol, h.mun
FROM hpq_aquani a
INNER JOIN hpq_hh h
ON (h.`main.id` = a.`main.id`)
WHERE aquanitype_o is not null and h.mun = 24
GROUP BY h.mun;


/*#3: How much carp has been caught with bagnets*/
select total_aquani_volume as volume, hpq_aquani.aquani_type as aquani_type, hpq_aquaequip.equip_type as equipment_type
from cube
inner join hpq_aquani on (cube.hpq_aquani_id = hpq_aquani.id) 
inner join hpq_aquaequip on (cube.hpq_aquaequip_id = hpq_aquaequip.id) 
where hpq_hh_id is null and hpq_aquani.id = 5 and hpq_aquaequip.id = 3
group by hpq_aquani.aquani_type, hpq_aquaequip.equip_type;

/*#3 CONVERTED*/
SELECT aquanitype, aquaequiptype, SUM(aquani_vol)
FROM hpq_aquani as a
INNER JOIN hpq_aquaequip e ON (e.`main.id` = a.`main.id`)
WHERE aquanitype = 5 and aquaequiptype = 3 and aquaequiptype_o is not null;


/*#4: How much fish was caught with fish nets in each municipality*/
select total_aquani_volume as volume, hpq_aquaequip.equip_type as equipment_type, hpq_hh.mun as municipality
from cube
inner join hpq_aquaequip on (cube.hpq_aquaequip_id = hpq_aquaequip.id) 
inner join hpq_hh on (cube.hpq_hh_id = hpq_hh.id) 
where hpq_aquani_id is null and hpq_aquaequip.id = 1
group by hpq_aquaequip.equip_type, hpq_hh.mun;

/* #4 CONVERTED*/
SELECT mun, aquaequiptype, SUM(aquani_vol)
FROM hpq_aquaequip as e
INNER JOIN hpq_hh as h ON (e.`main.id` = h.`main.id`)
INNER JOIN hpq_aquani as a ON (e.`main.id` = a.`main.id`)
WHERE e.aquaequiptype = 1 and a.aquanitype is not null
GROUP BY h.mun;


/*#5: How much catfish was caught in '5' with hooks and line*/
select total_aquani_volume as volume, hpq_hh.mun as municipality, hpq_aquani.aquani_type as aquani_type, hpq_aquaequip.equip_type as equipment_type
from cube
inner join hpq_hh on (cube.hpq_hh_id = hpq_hh.id) 
inner join hpq_aquani on (cube.hpq_aquani_id = hpq_aquani.id) 
inner join hpq_aquaequip on (cube.hpq_aquaequip_id = hpq_aquaequip.id) 
where hpq_aquani.id = 3 and hpq_aquaequip.id = 6 and hpq_hh.mun = 5
group by hpq_hh.mun, hpq_aquani.aquani_type, hpq_aquaequip.equip_type;

/*#5 CONVERTED*/
SELECT mun, aquanitype, aquaequiptype, aquani_vol
FROM hpq_aquaequip as e
INNER JOIN hpq_aquani as a ON (e.`main.id` = a.`main.id`)
INNER JOIN hpq_hh as h ON (e.`main.id` = h.`main.id`)
WHERE a.aquanitype = 3 and e.aquaequiptype = 6 and h.mun = 5
GROUP BY h.mun, a.aquanitype, e.aquaequiptype
