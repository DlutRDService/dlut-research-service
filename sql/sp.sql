DROP PROCEDURE IF EXISTS insert_or_update_paper;

DELIMITER $$

CREATE PROCEDURE insert_or_update_paper(
    IN p_tl VARCHAR(500),
    IN p_au TEXT,
    IN p_de TEXT,
    IN p_so VARCHAR(500),
    IN p_py VARCHAR(4),
    IN p_wc VARCHAR(500),
    IN p_esi VARCHAR(100),
    IN p_tc SMALLINT,
    IN p_nr SMALLINT,
    IN p_short_ab VARCHAR(1500),
    IN p_ab TEXT,
    IN p_doi VARCHAR(500),
    IN p_cr TEXT,
    IN p_research_background VARCHAR(2000),
    IN p_research_method VARCHAR(2000),
    IN p_research_result VARCHAR(2000),
    IN p_research_conclusion VARCHAR(2000)
)
BEGIN
    
    DECLARE existing_paper_id BIGINT;
    -- 判断是否存在
    SELECT paper.paper_id INTO existing_paper_id FROM paper WHERE paper.tl = p_tl LIMIT 1;

    IF existing_paper_id IS NULL THEN
        -- 插入paper表
        INSERT INTO paper (tl, au, de, so, py, wc, esi, tc, nr, ab, doi, cr, ab_id)
        VALUES (p_tl, p_au, p_de, p_so, p_py, p_wc, p_esi, p_tc, p_nr, p_short_ab, p_doi, p_cr, 0);

        -- 获取最新插入的paper_id
        SET @last_paper_id = LAST_INSERT_ID();

        -- 插入abstract表
        INSERT INTO abstract (paper_id, paper_doi, abstract, research_background, research_method, research_result, research_conclusion)
        VALUES (@last_paper_id, p_doi, p_ab, p_research_background, p_research_method, p_research_result, p_research_conclusion);

        -- 获取最新插入的abstract_id
        SET @last_abstract_id = LAST_INSERT_ID();

        -- 更新paper表的ab_id
        UPDATE paper SET ab_id = @last_abstract_id WHERE paper_id = @last_paper_id;
    END IF;

END$$

DELIMITER ;


-- 创建作者存储过程
DROP PROCEDURE IF EXISTS insert_or_update_author_record;

DELIMITER //

CREATE PROCEDURE insert_or_update_author_record(
    IN p_author_name VARCHAR(255),
    IN p_author_country VARBINARY(255),
    IN p_author_org VARCHAR(500)
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
        INSERT INTO author (author_name, author_country, author_org, paper_count, paper_count_per_year, H, high_cited_paper)
        VALUES (p_author_name, p_author_country, p_author_org, 1, NULL, 0, 0);
    ELSE
        -- 存在满足条件的记录，执行更新操作
        UPDATE author
        SET paper_count = paper_count + 1
        WHERE author_name = p_author_name AND author_org = p_author_org;
    END IF;

END //

DELIMITER ;