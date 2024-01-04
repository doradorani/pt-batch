package com.example.pass.respository;

import static org.junit.jupiter.api.Assertions.*;

import com.example.pass.respository.packaze.PackageEntity;
import com.example.pass.respository.packaze.PackageRespository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class PackageRespositoryTest {

    @Autowired
    private PackageRespository packageRespository;

    @Test
    public void test_save() {
        // given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디 챌린지 PT 12주");
        packageEntity.setPeriod(84);

        //when
        packageRespository.save(packageEntity);

        //then
        assertNotNull(packageEntity.getPackageSeq());

    }

    @Test
    public void test_findByCreatedAtAfter() {
        //given
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);

        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("학생 전용 3개월");
        packageEntity.setPeriod(90);
        packageRespository.save(packageEntity);

        PackageEntity packageEntity1 = new PackageEntity();
        packageEntity1.setPackageName("학생 전용 6개월");
        packageEntity1.setPeriod(180);
        packageRespository.save(packageEntity1);

        //when
        final List<PackageEntity> packageEntities = packageRespository.findByCreatedAtAfter(dateTime, PageRequest.of(0, 1, Sort.by("packageSeq").descending()));

        // then
        assertEquals(1,packageEntities.size());
        assertEquals(packageEntity1.getPackageSeq(), packageEntities.get(0).getPackageSeq());
    }

    @Test
    public void test_updateCountAndPeriod(){
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디프로필 이벤트 4개월");
        packageEntity.setPeriod(90);
        packageRespository.save(packageEntity);

        //when
        int updatedCount = packageRespository.updateCountAndPeriod(packageEntity.getPackageSeq(), 30, 120);
        final PackageEntity updatedPackageEntity = packageRespository.findById(packageEntity.getPackageSeq()).get();

        // then
        assertEquals(1, updatedCount);
        assertEquals(30, updatedPackageEntity.getCount());
        assertEquals(120, updatedPackageEntity.getPeriod());
    }

    @Test
    public void test_delete() {
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("제거할 이용권");
        packageEntity.setCount(1);
        PackageEntity newPackageEntity = packageRespository.save(packageEntity);

        //when
        packageRespository.deleteById(newPackageEntity.getPackageSeq());

        //then
        assertTrue(packageRespository.findById(newPackageEntity.getPackageSeq()).isEmpty());
    }
}
