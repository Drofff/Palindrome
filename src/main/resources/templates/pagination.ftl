<div>
	<button <#if page_number == 0>disabled</#if> class="mdl-button mdl-js-button mdl-button--primary" onclick="prev_page()">
		Попередня
	</button>
	<button class="mdl-button mdl-js-button mdl-button--primary">
        ${page_number + 1}/${pages_count}
	</button>
	<button <#if page_number gte (pages_count - 1)>disabled</#if> class="mdl-button mdl-js-button mdl-button--primary" onclick="next_page()">
		Наступна
	</button>
</div>