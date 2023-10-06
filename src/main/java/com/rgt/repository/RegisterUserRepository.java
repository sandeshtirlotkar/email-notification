package com.rgt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rgt.entity.RegisterUserEntity;

@Repository
public interface RegisterUserRepository extends JpaRepository<RegisterUserEntity, String>{

	@Query("SELECT new com.rgt.entity.RegisterUserEntity(r.username ,r.address , r.city ,r.pincode,r.dob) from RegisterUserEntity r") //WHERE TRUNC(CREATED_ON) >= trunc(sysdate-1)
	List<RegisterUserEntity> getRegisterData();

	boolean existsByEmailId(String emailId);

	//@Query(value = "select * from register_user where emailId= :emailId " , nativeQuery = true)
	RegisterUserEntity findByEmailId(@Param("emailId")String emailId);
	


}
