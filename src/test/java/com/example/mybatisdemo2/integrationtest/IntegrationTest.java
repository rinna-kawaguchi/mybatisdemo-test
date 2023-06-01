package com.example.mybatisdemo2.integrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DataSet(value = "names.yml")
    @Transactional
    void ユーザーが全件取得できステータスコード200が返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/names"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("[" +
                "{\"name\": \"清水\"}, " +
                "{\"name\": \"小山\"}, " +
                "{\"name\": \"田中\"}" +
                "]", response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "empty.yml")
    @Transactional
    void DBが空の時に空のListとステータスコード200が返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/names"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("[]", response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "names.yml")
    @Transactional
    void 指定したIDのユーザーとステータスコード200が返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/names/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("{\"id\": 1, \"name\": \"清水\"}", response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "names.yml")
    @Transactional
    void 指定したIDのユーザーが存在しない時に例外がスローされステータスコード404が返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/names/4"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("{" +
                        "\"timestamp\": \"2023-06-01T00:00:00.512623+09:00[Asia/Tokyo]\", " +
                        "\"Status\": \"404\", " +
                        "\"error\": \"Not Found\", " +
                        "\"message\": \"This id is not found\", " +
                        "\"path\": \"/names/4\"" +
                        "}",
                response,
                new CustomComparator(JSONCompareMode.STRICT,
                        new Customization("timestamp", (o1, o2) -> true))
        );
    }

    @Test
    @DataSet(value = "names.yml")
    @Transactional
    void 指定したIDより大きいIDのユーザーとステータスコード200が返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/selectnamegt?id=1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("[" +
                "{\"name\": \"小山\"}, " +
                "{\"name\": \"田中\"}" +
                "]", response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "names.yml")
    @Transactional
    void 指定したIDより大きいIDのユーザーが存在しない時に空のListとステータスコード200が返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/selectnamegt?id=3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("[]", response, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "names.yml")
    @ExpectedDataSet(value = "datasets/insert_names.yml", ignoreCols = "id")
    @Transactional
    void 名前が登録できステータスコード201が返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/names")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                {"name": "高橋"}
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(response).isEqualTo("user successfully created");
    }

    @Test
    @DataSet(value = "names.yml")
    @Transactional
    void 登録時のリクエスト項目にnullがある時に例外がスローされステータスコード400が返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/names")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                {}
                                """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                        {
                        "timestamp": "2023-06-01T00:00:00.512623+09:00[Asia/Tokyo]", 
                        "Status": "400", 
                        "error": "Bad Request", 
                        "message": "Please enter your name", 
                        "path": "/names"
                        }
                        """,
                response,
                new CustomComparator(JSONCompareMode.STRICT,
                        new Customization("timestamp", (o1, o2) -> true))
        );
    }

    @Test
    @DataSet(value = "names.yml")
    @ExpectedDataSet(value = "datasets/update_names.yml")
    @Transactional
    void 名前が更新できステータスコード200が返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/names/3")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                {"name": "高橋"}
                                """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(response).isEqualTo("user successfully updated");
    }

    @Test
    @DataSet(value = "names.yml")
    @Transactional
    void 更新リクエストで存在しないIDを指定した時に例外がスローされステータスコード404が返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/names/4")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                {"name": "高橋"}
                                """))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("{" +
                        "\"timestamp\": \"2023-06-01T00:00:00.512623+09:00[Asia/Tokyo]\", " +
                        "\"Status\": \"404\", " +
                        "\"error\": \"Not Found\", " +
                        "\"message\": \"This id is not found\", " +
                        "\"path\": \"/names/4\"" +
                        "}",
                response,
                new CustomComparator(JSONCompareMode.STRICT,
                        new Customization("timestamp", (o1, o2) -> true))
        );
    }

    @Test
    @DataSet(value = "names.yml")
    @ExpectedDataSet(value = "delete_names.yml")
    @Transactional
    void 名前が削除できステータスコード200が返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.delete("/names/3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                {"message": "user successfully deleted"}
                """,
                response, JSONCompareMode.STRICT);
    }

    // 未作成
    @Test
    @DataSet(value = "names.yml")
    @Transactional
    void 存在しないIDを指定して削除しようとした場合に例外がスローされステータスコード404が返されること() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.delete("/names/4"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("{" +
                        "\"timestamp\": \"2023-06-01T00:00:00.512623+09:00[Asia/Tokyo]\", " +
                        "\"Status\": \"404\", " +
                        "\"error\": \"Not Found\", " +
                        "\"message\": \"This id is not found\", " +
                        "\"path\": \"/names/4\"" +
                        "}",
                response,
                new CustomComparator(JSONCompareMode.STRICT,
                        new Customization("timestamp", (o1, o2) -> true))
        );
    }
}
