set /p "iterations=Enter the number of iterations: "
set /p "wait=How long should be between each execution? [min]: "
set /a min=wait*60
setlocal enabledelayedexpansion

for /l %%x in (1, 1, !iterations!) do (
	echo "[%%x] Running collection..."
	postman login --with-api-key YOUR_POSTMAN_API_KEY
	postman collection run Vertretungsplan.postman_collection.json -e Vertreungsplan.postman_environment.json
	if %%x neq %iterations% (
		timeout %min%
	)
)
endlocal
pause
