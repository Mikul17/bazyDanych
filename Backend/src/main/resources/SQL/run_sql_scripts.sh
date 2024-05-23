#!/bin/bash

# Sprawdź, czy wszystkie wymagane zmienne środowiskowe są ustawione
if [ -z "$DB_NAME" ] || [ -z "$MYSQL_HOST" ] || [ -z "$MYSQL_USER" ] || [ -z "$MYSQL_PASSWORD" ] || [ -z "$MYSQL_PORT" ]; then
    echo "You need to define following environmental variables: MYSQL_HOST, MYSQL_PORT, MYSQL_USERNAME, MYSQL_PASSWORD, DB_NAME"
    exit 1
fi

# Lista plików SQL w odpowiedniej kolejności
SQL_FILES=(
    "League.sql"
    "Team.sql"
    "Defender.sql"
    "Midfielder.sql"
    "Forward.sql"
    "Goalkeeper.sql"
    "Defender_skill.sql"
    "Midfielder_skill.sql"
    "Forward_skill.sql"
    "Goalkeeper_skill.sql"
    "Player_stat.sql"
    "Captain.sql"
    "Bet_type.sql"
)

# Funkcja do wykonania pliku SQL
execute_sql_script() {
    local file=$1
    mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$DB_NAME" < "$file"
    if [ $? -ne 0 ]; then
        echo "Wystąpił błąd podczas wykonywania skryptu: $file"
        exit 1
    fi
}

# Główna pętla wykonująca skrypty SQL
for sql_file in "${SQL_FILES[@]}"; do
    if [ -f "$sql_file" ]; then
        echo "Wykonywanie skryptu: $sql_file"
        execute_sql_script "$sql_file"
    else
        echo "Plik nie istnieje: $sql_file"
        exit 1
    fi
done

echo "Wszystkie skrypty zostały wykonane pomyślnie."