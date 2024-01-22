package com.cba.core.wirememobile.dao;

import com.cba.core.wirememobile.model.Merchant;

public interface MerchantDao {

    Merchant findByMerchantId(String merchantId) throws Exception;
}
