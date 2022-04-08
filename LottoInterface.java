package com.lotto.model;

import java.util.List;

public interface LottoInterface {
	public void insert(LottoVO lottoVO);
	public List<LottoVO> getAll();
}
