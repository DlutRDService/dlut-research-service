DROP PROCEDURE IF EXISTS insert_or_update_paper;

DELIMITER //

CREATE PROCEDURE insert_or_update_paper(
    IN input_tl VARCHAR(500),
    IN input_au TEXT,
    IN input_de TEXT,
    IN input_so VARCHAR(255),
    IN input_py VARCHAR(4),
    IN input_wc VARCHAR(255),
    IN input_esi VARCHAR(100),
    IN input_tc SMALLINT,
    IN input_nr SMALLINT,
    IN input_ab TEXT,
    IN input_ab_path VARCHAR(500)
)
BEGIN
    DECLARE short_au VARCHAR(500);
    DECLARE long_au TEXT;
    DECLARE short_ab VARCHAR(1000);
    DECLARE long_ab TEXT;
    DECLARE short_de VARCHAR(500);
    DECLARE long_de TEXT;

    -- 处理第一个文本字段
    IF CHAR_LENGTH(input_au) <= 500 THEN
        SET short_au = input_au;
        SET long_au = NULL;
    ELSE
        SET short_au = LEFT(input_au, 500);
        SET long_au = input_au;
    END IF;

    -- 处理第二个文本字段
    IF CHAR_LENGTH(input_de) <= 500 THEN
        SET short_de = input_de;
        SET long_de = NULL;
    ELSE
        SET short_de = LEFT(input_de, 500);
        SET long_au = input_au;
    END IF;

    -- 处理第三个文本字段
    IF CHAR_LENGTH(input_ab) <= 1000 THEN
        SET short_ab = input_ab;
        SET long_ab = NULL;
    ELSE
        SET short_ab = LEFT(input_ab, 1000);
        SET long_ab = input_ab;
    END IF;

    -- 插入或更新记录
    INSERT INTO paper (tl, au, long_au, de, so, py, wc, esi, tc, nr, ab,long_ab, ab_path)
    VALUES (input_tl, short_au, long_au, input_de, input_so, input_py, input_wc,
            input_esi, input_tc, input_nr, short_ab, long_ab, input_ab_path);

END //

DELIMITER ;



-- 创建作者存储过程
DROP PROCEDURE IF EXISTS insert_or_update_author_record;

DELIMITER //

CREATE PROCEDURE insert_or_update_author_record(
    IN p_author_name VARCHAR(255),
    IN p_author_country VARBINARY(255),
    IN p_author_org VARCHAR(500),
    IN p_author_research VARBINARY(255)
)
BEGIN
    DECLARE v_count INT;

    -- 检查是否存在满足条件的记录
    SELECT author_id INTO v_count
    FROM author
    WHERE author_name = p_author_name AND author_org = p_author_org AND author_org IS NOT NULL
    LIMIT 1;

    SELECT v_count;

    IF v_count IS NULL THEN
        -- 不存在满足条件的记录，执行插入操作
        INSERT INTO author (author_name, author_country, author_org, paper_count, paper_count_per_year, research, H, high_cited_paper)
        VALUES (p_author_name, p_author_country, p_author_org, 1, NULL, p_author_research, 0, 0);
    ELSE
        -- 存在满足条件的记录，执行更新操作
        UPDATE author
        SET paper_count = paper_count + 1
        WHERE author_name = p_author_name AND author_org = p_author_org;
    END IF;

END //

DELIMITER ;