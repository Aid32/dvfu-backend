#!/usr/bin/env bash
BASE=http://localhost:8080

echo -e "\n=== PING ==="
curl -s -i $BASE/ping

echo -e "\n\n=== Equipment: create (ID 1) ==="
curl -s -i -X POST $BASE/equipment -H "Content-Type: application/json" -d '{"name":"Горный велосипед","type":"BIKE"}'

echo -e "\n\n=== Equipment: create (ID 2) ==="
curl -s -i -X POST $BASE/equipment -H "Content-Type: application/json" -d '{"name":"Палатка","type":"TENT"}'

echo -e "\n\n=== Member: create BASIC (ID 1) ==="
curl -s -i -X POST $BASE/members -H "Content-Type: application/json" -d '{"name":"Иван","membershipType":"BASIC"}'

echo -e "\n\n=== Member: create PREMIUM (ID 2) ==="
curl -s -i -X POST $BASE/members -H "Content-Type: application/json" -d '{"name":"Мария","membershipType":"PREMIUM"}'

echo -e "\n\n=== Rent: success (Иван берет велосипед) ==="
curl -s -i -X POST $BASE/members/1/rentals -H "Content-Type: application/json" -d '{"equipmentId":1}'

echo -e "\n\n=== Rent: equipment busy (Мария берет велосипед) -> 409 ==="
curl -s -i -X POST $BASE/members/2/rentals -H "Content-Type: application/json" -d '{"equipmentId":1}'

echo -e "\n\n=== Delete rented equipment -> 409 ==="
curl -s -i -X DELETE $BASE/equipment/1

echo -e "\n\n=== Delete member with rentals -> 409 ==="
curl -s -i -X DELETE $BASE/members/1

echo -e "\n\n=== Rent: bad json -> 400 ==="
curl -s -i -X POST $BASE/members/1/rentals -H "Content-Type: application/json" -d 'not-json'