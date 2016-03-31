create table Cube as

select hpq_hh_id, hpq_aquani_id, hpq_aquaequip_id, sum(aquani_vol) AS total_aquani_volume
from fact_table
group by hpq_hh_id, hpq_aquani_id, hpq_aquaequip_id with rollup

union

select hpq_hh_id, hpq_aquani_id, hpq_aquaequip_id, sum(aquani_vol) AS total_aquani_volume
from fact_table
group by  hpq_aquani_id, hpq_aquaequip_id, hpq_hh_id with rollup

union

select hpq_hh_id, hpq_aquani_id, hpq_aquaequip_id, sum(aquani_vol) AS total_aquani_volume
from fact_table
group by  hpq_aquaequip_id, hpq_hh_id, hpq_aquani_id with rollup